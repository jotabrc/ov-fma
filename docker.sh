#!/bin/bash
#curl -fsSL https://get.docker.com -o get-docker.sh
#sudo sh ./get-docker.sh
#sudo apt-get update
#sudo apt-get install docker-compose-plugin
## adds vagrant user to the docker group, sudo is no longer needed
#sudo usermod -aG docker vagrant
#docker --version
#docker compose version
#newgrp docker
#sudo systemctl enable docker
#sudo systemctl start docker

curl -fsSL https://get.docker.com | sudo bash
sudo curl -fsSL "https://github.com/docker/compose/releases/download/1.25.4/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
sudo usermod -aG docker vagrant