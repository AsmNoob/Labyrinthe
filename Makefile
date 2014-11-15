JFLAGS = -Xlint
JC = javac

CLASSES = *.java
OBJECT = $(CLASSES:.java=.class)

all: $(OBJECT)

$(OBJECT):$(CLASSES)
	$(JC) $(JFLAGS) $^

%.pdf:%.dot
	dot -Tpdf Graph.dot -o Graph.pdf

clean:
	$(RM) *.class


