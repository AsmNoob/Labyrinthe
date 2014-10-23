JFLAGS = -Xlint
JC = javac

CLASSES = Node.java Maze.java Graph.java Main.java Arc.java
OBJECT = $(CLASSES:.java=.class)

all: $(OBJECT)

$(OBJECT):$(CLASSES)
	$(JC) $(JFLAGS) $^

clean:
	$(RM) *.class