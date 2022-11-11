## Simple camelCase character generator
## Literally used because i was lazy

## Author: DiamondDev
## License: Creative Commons

charSet1 = 48
charSet1C = 10

charSet2 = 65
charSet2C = 26

charSet3 = 97
charSet3C = 26

###############

chars = []

pos = charSet1
for i in range(charSet1C):
    chars.append(chr(pos + i))

pos = charSet2
for i in range(charSet2C):
    chars.append(chr(pos + i))


pos = charSet3
for i in range(charSet3C):
    chars.append(chr(pos + i))

print(chars)