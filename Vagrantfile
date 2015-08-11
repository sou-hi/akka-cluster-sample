Vagrant.configure("2") do |config|
  config.vm.box = "centos6"
  #config.vm.network :forwarded_port, guest: 22, host: 2222, id: "ssh"
  config.vm.provision "shell", inline: <<-SHELL
    sudo yum install -y java-1.8.0-openjdk-devel
    sudo yum install -y unzip
    wget https://dl.bintray.com/sbt/native-packages/sbt/0.13.8/sbt-0.13.8.zip -O temp.zip; unzip temp.zip; rm temp.zip
    sudo ln -f -s $PWD/sbt/bin/sbt /usr/local/bin/sbt
  SHELL

  config.vm.define "cluster1" do |cluster|
    cluster.vm.network "private_network", ip: "192.168.33.10"
  end

  config.vm.define "cluster2" do |cluster|
    cluster.vm.network "private_network", ip: "192.168.33.11"
  end

  config.vm.define "cluster3" do |cluster|
    cluster.vm.network "private_network", ip: "192.168.33.12"
  end
end