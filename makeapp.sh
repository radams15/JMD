#!/bin/bash

NAME=JMD
OUT=${NAME}.app
echo $OUT

mkdir -p $OUT/Contents/MacOS
mkdir -p $OUT/Contents/Resources

cat << EOF > ${OUT}/Contents/MacOS/${NAME}
#!/bin/bash

DIR=/Applications/${OUT}

java -Xdock:name="$NAME" -jar $$DIR/Contents/Resources/${NAME}.jar
EOF

chmod +x ${OUT}/Contents/MacOS/${NAME}

cp target/$NAME*.jar ${OUT}/Contents/Resources/${NAME}.jar

zip -r JMD.zip JMD.app/