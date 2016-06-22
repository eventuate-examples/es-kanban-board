#! /bin/bash -e

sudo apt-get update
sudo apt-get install -y apt-transport-https ca-certificates
sudo apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys 58118E89F3A912897C070ADBF76221572C52609D
sudo bash -c 'echo "deb https://apt.dockerproject.org/repo ubuntu-wily experimental" > /etc/apt/sources.list.d/docker.list'
sudo apt-get update
apt-cache policy docker-engine
sudo apt-get install -y docker-engine
sudo service docker start
sudo bash -c 'curl -L https://github.com/docker/compose/releases/download/1.6.2/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose'
sudo chmod +x /usr/local/bin/docker-compose

