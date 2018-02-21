# Program, flags, etc.
JAC      = javac
JAR      = jar
OBJ     = ./*
STATOBJ = stat/*.class
TARGET  = game rank survey
OUTDIR	= out

all: clean precompile $(TARGET)

clean:
	-rm -r out stat *.jar

precompile:
	mkdir $(OUTDIR); cp -r src/res $(OUTDIR); $(JAC) -cp lib/* src/*/*.java -encoding Big5 -d $(OUTDIR)

game: precompile
	cd $(OUTDIR); $(JAR) xf ../lib/core.jar; $(JAR) cfe ../Game.jar main.MainApp $(OBJ)

rank: precompile
	cd $(OUTDIR); $(JAR) cfe ../Rank.jar stat.Rank $(STATOBJ) res/rank/

survey: precompile
	cd $(OUTDIR); $(JAR) cfe ../Survey.jar stat.Survey $(STATOBJ)