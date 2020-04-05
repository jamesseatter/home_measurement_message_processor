# Setting up a new IOT Device using Raspbian

##Change the hostname of the system

##Enable SSH

##Add IOT User
[https://www.digitalocean.com/community/tutorials/how-to-create-a-sudo-user-on-ubuntu-quickstart]

`sudo adduser iot `


##Give user SUDO Access
[https://www.digitalocean.com/community/tutorials/how-to-create-a-sudo-user-on-ubuntu-quickstart]

`sudo usermod -aG sudo <username>`


##Create IOT folder structure
under /home/\<username> create the following
```
mkdir bin/<collector or gateway>_test
mkdir bin/<collector or gateway>_prod
```

##Configure SystemD
###Step 1 - Unit configuration file
  Location -  /lib/systemd/system
  
  File Name - \<messageprocessor>_\<env>.service.
  
  ex. `collector_test.service`

###Step 2 - Security

  sudo chmod +x /home/iot/bin/messageprocessor_\<env>/messageprocessor.jar.   
  `sudo chmod +x /home/iot/bin/messageprocessor_test/messageprocessor.jar`


###Step 3 - Configure SystemD
copy from the systemd folder provided by the deployment to <location> above.
`sudo cp systemd/messageprocessor_test.service /lib/systemd/system`

`sudo systemctl daemon-reload`

sudo systemctl enable messageprocessor_\<env>

ex. `sudo systemctl enable messageprocessor_test`
