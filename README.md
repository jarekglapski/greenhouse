# greenhouse
## Usage:
### Preconditions:
1. Raspbian - install & update
2. clone https://github.com/adafruit/Adafruit_Python_DHT & follow the installation instructions
3. make AdafruitDHT available in Path:
```
sudo cp Adafruit_Python_DHT/examples/AdafruitDHT.py /usr/bin/AdafruitDHT
```
4. Test modules:
```
modprobe w1-gpio
modprobe w1-therm
```
5. Check loaded modules:
```
lsmod | grep w1
```
6. Add modules for auto loading:
```
echo "w1-gpio" >> /etc/modules
echo "w1-therm" >> /etc/modules
```
