やりたいこと

- README.md 追記：このソフトは、一時的にパソコンをスリープしない状態にするのにも役立ちます。
- 通知画面、メッセージ生成のロジックを通知とは分けたい。文字化けを防止したい。そもそもあの文字使わなければいい？
- 注意喚起、とちょっと大きめな字で表示したい。

export MAVEN_OPTS="-Dhttps.protocols=TLSv1.2 -Djavax.net.ssl.trustStoreType=JKS -Djava.net.preferIPv4Stack=true"
mvn clean package

