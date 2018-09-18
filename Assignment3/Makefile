JAVAC=/usr/bin/javac
JAVA=/usr/bin/java
.SUFFIXES: .java .class

SRCDIR=src
BINDIR=bin

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<

CLASSES= Tree.class FileUtil.class ParallelTask.class ParallelProgram.class
CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)

default: $(CLASS_FILES)

make run:
	$(JAVA) -cp $(BINDIR)/ ParallelProgram
clean:
	rm $(BINDIR)/*.class

javadoc:
	javadoc -d doc $(SRCDIR)/*.java



	
	


