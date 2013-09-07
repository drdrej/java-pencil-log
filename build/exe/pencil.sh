# OS specific support.  $var _must_ be set to either true or false.
cygwin=false
os400=false
case "`uname`" in
CYGWIN*) cygwin=true;;
OS400*) os400=true;;
esac

if $os400; then
  echo "PENCIL does not support OS400 (this .sh script is not written and tested for OS400)"
fi

# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
  JAVA_HOME=`cygpath --absolute --windows "$JAVA_HOME"`
  PENCIL_HOME=`cygpath --absolute --windows "$PENCIL_HOME"``
  CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
  JAVA_ENDORSED_DIRS=`cygpath --path --windows "$JAVA_ENDORSED_DIRS"`
fi


if [ -z "$JAVA_HOME" ]; then
  echo "The JAVA_HOME environment variable is not defined"
  echo "This environment variable is needed to run this program"
  exit 1
fi


if [ ! -x "$JAVA_HOME"/bin/java ]; then
  echo "The JAVA_HOME environment variable is not defined correctly"
  echo "This environment variable is needed to run this program"
  echo "NB: JAVA_HOME should point to a JDK or JRE"
  exit 1
fi


exec $JAVA_HOME"/bin/java -cp -jar "$PENCIL_HOME\lib\pencil-core.jar" "$@" start
