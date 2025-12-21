TODO

- Separate notification UI from message generation logic to reduce encoding issues.
- Show warning messages with slightly larger font.

Build note:

export MAVEN_OPTS="-Dhttps.protocols=TLSv1.2 -Djavax.net.ssl.trustStoreType=JKS -Djava.net.preferIPv4Stack=true"
mvn clean package
