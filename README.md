# Sercer
Software for training and scenario generation within lifesaving sport.

Sercer is a piece of software designed to be a tool for lifesavers such that they can expand their skill-set by auto-generating
new Simulated Emergency Response Competitions (SERCs). This is also designed to be a framework for SERC writers in order to 
speed up the process for training and competition.

## Running
In order to run the software, **Java Runtime 8** is needed. Currently in the process of updating the project to work on the
latest JRE. Downloading `Sercer.jar` at the root of the project path is the current most stable format. This can be ran either
by clicking on the file once downloaded or via terminal, simply by typing:

`java -jar Sercer.jar`

This should then start the main window which will look very blank. 


## Features
The main features of the software is that scenarios can be created and generated quickly for use in practical training. Not 
only this, but the SERCs created can then be saved as `.pdf` or `.serc` files. The former is so that printing and distribution
can be done, the latter so that you can load a previously saved SERC into the software.

### Create Window
The create window is so that you can draw your own SERC, the idea behind this is because you already know what you
want to be in the SERC, this is simply a tool for you to draw out quickly as a lot of SERCs take a while to draw out.
Pressing 'convert' will remove the boarder around the cells and pass to the main window. It is also kept in the create
window so that more changes can be made as you go along.

Sadly, re-sizing the window has been disabled, this is because the actual canvas in which you paint the SERC will not
re-size along with it, it will stay the same size. This is where the slider comes in, setting a smaller value on the
slider allows smaller cells, but can be harder to create what you want. Be warned; changing the slider when you already
have work made will remove all your work, convert it first.

Simple paint colours are used to represent common items in SERCS; Casualties, aids, water, walls, blank spaces (etc).
You won't be able to create multiple casualties or aids through dragging the mouse when clicked down, this is disabled as
it would be mis-representative of the object at hand.


### Generate Window
The generate window is the real interesting part. Here, SERCs can be generated with a specified amount of degree.
A lot of flexibility has been put into this, allowing only a few constraints you have to satisfy:

You are perfectly able to not enter anything and allow a completely random SERC to be created, this will be shown
back onto the main window. The 'Re-load' button will have become enabled now as well, as the type of SERC and number
of casualties has been set, pressing re-load will simply change:

1. The layout of the type of SERC (i.e. the surroundings will look different)
2. The placement of casualties and aids

If you wish to make a new random SERC, open the generate window and press generate.

If you put a number into the number of casualties, the algorithm will map all of the conditions you select to this numbe
this does allow 1 casualty to have every condition possible. It is down to you to decide what seems too unrealistic.
If you set a number of casualties and only put some conditions, then the algorithm will then allocate random conditions
for the remaining casualties. This could mean there are casualties with repeated conditions, and some casualties can be
simply not suffering.

If you have made a selection and wish to start again, simply close the window and press 'generate' on the main window
again.



