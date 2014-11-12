JFLAGS = -Xlint 
JC = javac

CLASSES = *.java
OBJECT = $(CLASSES:.java=.class)

all: $(OBJECT)

$(OBJECT):$(CLASSES)
	$(JC) $(JFLAGS) $^

clean:
	$(RM) *.class


