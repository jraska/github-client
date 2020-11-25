gcloud functions deploy github-client-webhooks --entry-point com.jraska.devanalytics.github.GitHubEventsMixpanelFunction --runtime java11 --memory 256MB --trigger-http --allow-unauthenticated
