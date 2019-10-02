# /bin/bash
./gradlew sonarqube \
  -Dsonar.projectKey=dropbox \
  -Dsonar.organization=giovane \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.login=1f8516f7b5f4cfb1ca63171342627c77290bdc26