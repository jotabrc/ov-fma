# -*- mode: ruby -*-
# vi: set ft=ruby  :

machines = {
  "master" => {"memory" => "1024", "cpu" => "1", "ip" => "101", "image" => "bento/ubuntu-24.04"},
  "database" => {"memory" => "1024", "cpu" => "1", "ip" => "103", "image" => "bento/ubuntu-24.04"}
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

      if "#{name}" == "master"
        machine.vm.provision "shell", path: "master.sh"
        machine.vm.provision "shell", path: "additional-setup.sh"
      else
        machine.vm.provision "shell", path: "worker.sh"
      end

        config.vm.synced_folder "/home/joao/Projects/ov-fma", "/data/ov-fma", type: "rsync", rsync__exclude: [".vagrant"]
    end
  end
end