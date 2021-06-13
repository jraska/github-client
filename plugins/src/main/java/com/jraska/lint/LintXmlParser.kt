package com.jraska.lint

import com.jraska.module.ModuleMetadata
import groovy.util.Node
import groovy.util.NodeList
import groovy.xml.XmlParser

class LintXmlParser(
  private val moduleMetadata: ModuleMetadata,
) {
  fun parse(lintXml: String): List<LintIssue> {
    val testSuiteNode = XmlParser().parseText(lintXml)

    return (testSuiteNode.get("issue") as NodeList)
      .map { it as Node }
      .map { parseIssue(it) }
  }

  private fun parseIssue(node: Node): LintIssue {
    return LintIssue(
      moduleMetadata,
      id = node.attributeString("id"),
      severity = node.severity(),
      category = node.attributeString("category"),
      summary = node.attributeString("summary"),
      priority = node.attributeInt("priority"),
      message = node.attributeString("message").trimToMaxLength(),
      errorLine = node.attribute("errorLine1")?.toString()?.trimToMaxLength(),
      location = location(node)
    )
  }

  private fun String.trimToMaxLength(): String {
    val fullMessage = this

    if (fullMessage.length > MAX_TEXT_LENGTH) {
      return "${fullMessage.substring(0, MAX_TEXT_LENGTH)}..."
    } else {
      return fullMessage
    }
  }

  private fun location(issueNode: Node): String? {
    val locationNode = (issueNode.get("location") as NodeList).firstOrNull() as Node? ?: return null

    val absolutePath = locationNode.attributeString("file")

    val pathIndex = absolutePath.indexOf(moduleMetadata.moduleName)
    return if (pathIndex == -1) {
      absolutePath
    } else {
      absolutePath.substring(pathIndex)
    }
  }

  private fun Node.attributeString(name: String): String {
    return attribute(name).toString()
  }

  private fun Node.severity(): Severity {
    val severityString = attributeString("severity")
    return when (severityString) {
      "Error" -> Severity.Error
      "Warning" -> Severity.Warning
      "Information" -> Severity.Info
      "Ignore" -> Severity.Ignore
      "Fatal" -> Severity.Fatal
      else -> throw IllegalArgumentException("Unknown severity: $severityString")
    }
  }

  private fun Node.attributeInt(name: String): Int {
    return attribute(name)?.toString()?.toInt() ?: 0
  }

  companion object {
    private val MAX_TEXT_LENGTH = 200
  }
}
