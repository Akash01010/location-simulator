import matplotlib.pyplot as plt
import csv

x = []
y = []

with open('points.csv','r') as csvfile:
    lines = csv.reader(csvfile, delimiter=',')
    for row in lines:
        x.append(row[0])
        y.append(row[1])

plt.plot(x, y, color = 'g', linestyle = 'dashed',
         marker = '_',label = "Path")

plt.xticks(rotation = 25)
plt.xlabel('Dates')
plt.ylabel('Temperature(°C)')
plt.title('Path', fontsize = 20)
plt.grid()
plt.legend()
plt.show()
