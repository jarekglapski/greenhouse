# greenhouse
Usage:
Install Raspbian - update
https://github.com/adafruit/Adafruit_Python_DHT - follow the installation instructions
sudo cp Adafruit_Python_DHT/examples/AdafruitDHT.py /usr/bin/AdafruitDHT
Test modules:
modprobe w1-gpio
modprobe w1-therm
Check loaded modules:
lsmod | grep w1
Add modules for auto loading:
echo "w1-gpio" >> /etc/modules
echo "w1-therm" >> /etc/modules
