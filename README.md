# S3 CSE Java

- utility to upload files to S3 by Client Side Encryption using Java SDK
- sample starter code for Java basic app plus test with junit 5 and mockito

## Setup Java11

```bash
# install adopt openjdk 11 by Mac jenv
brew cask install adoptopenjdk11
/usr/libexec/java_home -V
jenv add /Library/Java/JavaVirtualMachines/adoptopenjdk-11.jdk/Contents/Home
jenv versions
jenv global openjdk64-11.0.7

# check
java -version
> openjdk version "11.0.7" 2020-04-14
echo $JAVA_HOME
> ~/.jenv/versions/openjdk64-11.0.7
```

## Compile and Run

```bash
mvn package  # -DskipTests
cp target/s3cse-1.0-SNAPSHOT-jar-with-dependencies.jar ./bin/

./bin/s3cse upload s3-cse-key foo.txt s3://some-bucket/cse-test/
./bin/s3cse download s3-cse-key s3://some-bucket/cse-test/foo.txt .
```

## Note

```bash
# initial setup from official doc
mvn -B archetype:generate \
  -DarchetypeGroupId=org.apache.maven.archetypes \
  -DgroupId=org.example.basicapp \
  -DartifactId=s3cse
cd s3cse
# fix java.version to 11 in properties
mvn package  # -DskipTests
java -cp target/s3cse-1.0-SNAPSHOT.jar org.example.basicapp.App
> Hello World!
```

## Reference

- Upload client side encrypted file to S3
  - https://interworks.com.mk/amazon-rds-for-sql-server-encrypted-backuprestore/
- Official Doc
  - https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/SQLServer.Procedural.Importing.html
  - https://aws.amazon.com/blogs/database/client-side-encryption-and-decryption-of-microsoft-sql-server-backups-for-use-with-amazon-rds/
- Java SDK initial setup doc
  - https://aws.amazon.com/sdk-for-java/
  - https://aws.amazon.com/developers/getting-started/java/
  - https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-project-maven.html
- Java SDK sample code
  - https://docs.aws.amazon.com/ja_jp/AmazonS3/latest/dev/UsingClientSideEncryption.html
  - https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/examples-crypto-kms.html
  - https://aws.amazon.com/articles/client-side-data-encryption-with-the-aws-sdk-for-java-and-amazon-s3/
- terraform
  - https://qiita.com/minamijoyo/items/d68f162bd29cd3d766e8
  - https://www.terraform.io/docs/providers/aws/r/kms_key.html
  - https://www.terraform.io/docs/providers/aws/r/kms_alias.html
