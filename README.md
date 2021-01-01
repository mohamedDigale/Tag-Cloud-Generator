# Tag-Cloud-Generator
Summary: A Java program that takes in a text file and outputs a html file and generates a html file for a tag cloud of N most used words. The size of the words correspond with the number of times the word is used.

Requirements/Description:

The program shall ask the user for the name of an input file, for the name of an output file, and for the number of words to be included in the generated tag cloud (a positive integer, say N). 

The program shall respect the user input as being the complete relative or absolute path as the name of the input file, or the name of the output file, and will not augment the given path in any way, e.g., it will not supply its own filename extension. For example, a reasonable user response for the name of the input file could directly result in the String value "data/importance.txt"; similarly, a reasonable user response for the name of the output file could directly result in the String value "data/importance.html".

In contrast with one or more past projects, the program shall check for invalid input; however, the program may (and probably should) rely on the SimpleReader and SimpleWriter family components to raise an error in response to conditions such as non-existent files or paths.

The input file can be an arbitrary text file. No special requirements are imposed.

The output shall be a single well-formed HTML file displaying the name of the input file in a heading followed by a tag cloud of the N words with the highest count in the input. 

The words shall appear in alphabetical order (in which, e.g., "bar" comes before "Foo", not the lexicographic order provided by the String compareTo method which would put capitalized words ahead of lower case ones, e.g., "Foo" would come before "bar"). The font size of each word in the tag cloud shall be proportional to the number of occurrences of the word in the input text (i.e., more frequent words will be displayed in a larger font than less frequent ones).

Words contain no whitespace characters. Beyond that, it is up to you to come up with a reasonable definition of what a word is and what characters (in addition to whitespace characters) are considered separators. (For the sample inputs provided, the characters in the string " \t\n\r,-.!?[]';:/()" do a decent job of separating words.)
