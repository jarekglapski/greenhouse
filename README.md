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
sudo modprobe w1-gpio
sudo modprobe w1-therm
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
7. Connect your DS18B20 and DHT sensors to GPIO
8. Test DHT sensors & AdafruitDHT library from commandline:
```
sudo AdafruitDHT [type] [pin]
example: sudo AdafruitDHT 11 22 - Read from an DHT11 connected to GPIO #22
```