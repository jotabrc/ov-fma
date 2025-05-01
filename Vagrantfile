# -*- mode: ruby -*-
# vi: set ft=ruby  :

machines = {
  "master" => {"memory" => "512", "cpu" => "1", "ip" => "100", "image" => "bento/ubuntu-24.04"},
  "node01" => {"memory" => "512", "cpu" => "1", "ip" => "101", "image" => "bento/ubuntu-24.04"},
  "database" => {"memory" => "512", "cpu" => "1", "ip" => "102", "image" => "bento/ubuntu-24.04"}
}

Vagrant.configure("2") do |config|

  machines.each do |name, conf|
    config.vm.define "#{name}" do |machine|
      machine.vm.box = "#{conf["image"]}"
      machine.vm.hostname = "#{name}"
      machine.vm.network "private_network", ip: "192.168.56.#{conf["ip"]}"
      machine.vm.provider "virtualbox" do |vb|
        vb.name = "#{name}"
        vb.memory = conf["memory"]
        vb.cpus = conf["cpu"]

      end
      machine.vm.provision "shell", path: "docker.sh"

      machine.vm.provision "shell", inline: <<-SHELL
        export VM_IP=$(ip -o -4 addr show enp0s8 | awk '{print $4}' | cut -d/ -f1)
        echo "Accessible VM IP is: $VM_IP"

        echo 'export VM_IP=$(ip -o -4 addr show enp0s8 | awk "{print \$4}" | cut -d/ -f1)' >> /home/vagrant/.bashrc
      SHELL

      if "#{name}" == "master"
        machine.vm.provision "shell", path: "master.sh"
      else
        machine.vm.provision "shell", path: "worker.sh"
      end

    config.vm.synced_folder "/home/joao/Projects/ov-fma", "/data/ov-fma", type: "rsync"

    end
  end
end