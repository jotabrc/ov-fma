**João Carlos Roveda Ostrovski**</br>
**System name**: OV-FMA \- Ostro Veda Finance Management Application (Aplicação de Gerenciamento Financeiro)  
**Version**: *

### Project description
Financial management; income, expenditure, tracking; financial budget and investment registration.  
Services are used and deployed in four modules:

1. **Gateway**: all requests need to be directed to the gateway for proper authorization; services will deny access otherwise.
2. **User**: registration, user data querying and updates (new password, email, username, etc.),
3. **Authentication**: user authentication;
4. **Finance**: Finance Manager API for registering, querying and updating financial information;
    1. Requires a registered and authenticated user (JWT).

### Settings
VM requires `$VM_IP` environment variable for Gateway Swagger OpenAPI customizer.
- Swagger documentation in back-end services will send requests to the gateway using the virtual machine ip, otherwise the request fail for missing `X-Secure-Token` generated at the gateway and required at the back-end services.

Vagrantfile configures the set-up of the virtual machine, to use open the terminal in the project directory and use the command `vagrant up`, this will create an Ubuntu Server with 1 cpu, 512mb of ram and 5gb of disk space.<br/>
- the vagrant-default-install.sh will be execute automatically in Vagrant VM creating, installing Docker.

Docker can be used in the VM opening the terminal in the project root directory using the command `docker-compose up --build`, if desired with `-d` optional for running in the background.</br>

### Diagram
![diagram.png](docs/diagram.png)

##### Status
_work in progress_