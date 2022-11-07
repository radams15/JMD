OS ?= linux

ifeq (${OS}, linux)
JAVA_HOME ?= /opt/java/openjdk/
OUT_FILE ?= libmarkdown.so
endif

ifeq (${OS}, darwin)
JAVA_HOME ?= /System/Library/Frameworks/JavaVM.framework/Headers/
OUT_FILE ?= libmarkdown.dylib
endif

SCIDOWN_SRC =  scidown/src/utils.c   scidown/src/constants.c   scidown/src/autolink.c   scidown/src/buffer.c   scidown/src/document.c   scidown/src/escape.c   scidown/src/html_blocks.c   scidown/src/html.c   scidown/src/latex.c   scidown/src/html_smartypants.c   scidown/src/stack.c   scidown/src/version.c   scidown/src/charter/src/clist.c   scidown/src/charter/src/parser.c   scidown/src/charter/src/charter.c   scidown/src/charter/src/svg.c   scidown/src/charter/src/latex.c   scidown/src/charter/src/charter_string.c   scidown/src/charter/src/svg_utils.c   scidown/src/charter/src/tinyexpr/tinyexpr.c   scidown/src/charter/src/csv_parser/csvparser.c
CFLAGS = -I scidown/src

linux:
	gcc -shared -fPIC ${CFLAGS} -I ${JAVA_HOME}/include/linux -I ${JAVA_HOME}/include/ uk_co_therhys_MD_Converter.c ${SCIDOWN_SRC} -o ../resources/${OUT_FILE}

darwin:
	gcc -dynamiclib -fPIC ${CFLAGS} -I ${JAVA_HOME} uk_co_therhys_MD_Converter.c ${SCIDOWN_SRC} -o ../resources/${OUT_FILE}
