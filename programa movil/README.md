# **Proyecto #0**


## **Implementación y Automatización de Servicios de Red**
---
**Tecnológico de Costa Rica**


**Escuela de Ingeniería en Computación**


**Redes (IC 7602)**


---


**Estudiantes:**


- Luis Oswaldo Ramírez Fernández
- Max Richard Lee Chung
- Kelvin Núñez Barrantes
- Kenny Vega Obando
- Kaled Sánchez Vargas


**Profesor:**
- Gerardo Nereo Campos Araya


---


## Índice  
 
  - [Introducción](#introducción)
  - [Instalación del servidor](#instalación-del-servidor)
  - [Configuración de Servicios](#configuración-de-servicios)
  - [Pruebas Realizadas](#pruebas-realizadas)
  - [Recomendaciones y Conclusiones](#conclusiones)
  - [Componentes Implementados o Configurados](#componentes-implementados-o-configurados)
  - [Referencias Bibliográficas](#referencias-bibliográficas)




## **Introducción**
La automatización de la infraestructura, facilitada por herramientas como Terraform, Chef, Puppet o Ansible, es un pilar fundamental para la creación de redes en la actualidad y facilita los procesos en gran medida en diversos aspectos. La habilidad de definir y gestionar la infraestructura como código(IaC) permite una implementación más eficiente, escalable y reproducible, acelerando los diversos procesos involucrados. 




En este documento, se proporciona instrucciones claras y detalladas sobre cómo ejecutar cada una de las etapas del proyecto, desde la creación de la red virtual y la instalación automatizada de los servicios, hasta la realización de pruebas y la presentación de resultados. Se han incluido recursos visuales, como imágenes y diagramas, con el propósito de facilitar la comprensión y la ejecución del proyecto.


## Versiones utilizadas
Máquina local: Ubuntu 20.04.6 LTS
Terraform: 1.5.5
Ansible: Core 2.13.11
Instancias: Ubuntu 22.04.2 LTS


## Forma de trabajo
El equipo de reunió varias veces mediante llamadas, trabajando todos en conjunto mientras uno de los miembros se encargaba de aplicar los cambios realizados, debido a esto último los commits al repositorio vienen de pocos usuarios y no del equipo completo, sin embargo todos realizaron los aportes correspondientes


## **Instalación del servidor**
Antes de proceder con la instalación del servidor, es necesario asegurarse de cumplir con ciertos requisitos previos, a continuación, se detallan los pasos necesarios para preparar el entorno y llevar a cabo la instalación:


### Cuenta de Oracle y OCI Terraform:


Asegúrese de contar con una cuenta activa en Oracle para poder proceder con los siguientes pasos de instalación, también es necesario tener OCI Terraform instalado para ejecutar los scripts. 
Si no cuenta con el software, puede consultar el tutorial disponible en las referencias.


Antes de comenzar con la instalación, se requiere generar una llave privada y su correspondiente llave pública utilizando los comandos para la configuración de OCI:


>* openssl genrsa -out tu-directorio-home/.oci/nombre-de-tu-llave-rsa.pem 2048
>* chmod 600 tu-directorio-home/.oci/nombre-de-tu-llave-rsa.pem
>* openssl rsa -pubout -in tu-directorio-home/.oci/nombre-de-tu-llave-rsa.pem -out $HOME/.oci/nombre-de-tu-llave-rsa_public.pem

Luego esta llave se debe colocar en las API Keys de su usuario OCI


### Preparación del Directorio de Infraestructura:
Abra una consola de comandos y navegue al directorio que contiene los archivos de infraestructura del proyecto mediante el comando cd "ruta-de-tu-directorio"


### Prerrequisitos adicionales
Además de los pasos anteriores, se presentan algunos pasos a seguir en el proceso de configuración y uso del proyecto:


>* Asegúrese de tener a mano la información relevante proporcionada, como OCIDs, direcciones IP y otros datos necesarios para la configuración.
>* Verificar de tener instaladas las versiones de Terraform (v1.5.5) y Ansible (versión [core 2.13.11]). Además, configure su cuenta de OCI y API key.







### Ejecución de Terraform:


Desplazarse a la carpeta Terraform/tf-compartment y cambie todos los campos comprendidos en <> con la información recopilada anteriormente en los diferentes archivos. Posteriormente debe crear una llave ssh para ingresar a las instancias. Luego ejecute el siguiente comando para inicializar Terraform, planificar la infraestructura y aplicar los cambios automáticamente:


```bash
terraform init && terraform plan && terraform apply -auto-approve
```
> Nota: Asegúrese guardar el <Compartment-OCID>


Después de esto, desplazarse a la carpeta Terraform y hacer los mismos cambios de campos, para finalmente ejecutar el mismo comando de antes.
> Nota: Asegúrese de guardar la ip pública y privada


## Componentes Implementados o Configurados
### Ansible Apache
Antes de generar algún cambio es necesario realizar los siguientes pasos para asegurar la conexión a las instancias previamente creadas: 


Conectarse a la publica
>ssh -i <shh-private-key-path> ubuntu@<public_instance_public_ip>


Crear directorio en la pública 
>mkdir /home/ubuntu/opc/


Copiar la llave de la local a la pública desde la local
>scp -i <shh-private-key-path> <shh-private-key-path>>ubuntu@<public_instance_public_ip>:/home/ubuntu/opc/vnc_key


Conectarse a la privada
>ssh -i /home/ubuntu/opc/vnc_key ubuntu@<private_instance_private_ip>


Al poder establecer conexión con el servidor, se debe proceder a trasladarse a la carpeta “Ansil Apache”, cambiar todos los campos comprendidos por <> por los datos previamente recolectados y finalmente ejecutar el siguiente comando:
```
ansible-playbook -i inventory.ini setup_apache.yml
```


### Ansible Asterisk


Al poder instalar apache y los servidores, se crearon un par de scripts para instalar y configurar el Asterisk para que FreePBX pueda funcionar y por ende, las llamadas telefónicas.


El primer script abarca la instalación y el segundo la configuración de Asterisk, lo cual es crucial para el funcionamiento integral del proyecto, esta configuración permite que el servidor actúe como una central en el entorno deseado. Se debe trasladar a la carpeta de “FreePBX-Asterisk” y el proceso será el siguiente:


Primeramente, al igual que en la sección de apache, se debe cambiar todos los campos comprendidos por <> por los datos previamente recolectados en el archivo .ini y luego ejecutar:

```
ansible-playbook -i inventory.ini install_asterisk.yml
```

En este punto, Asterisk está parcialmente instalado y se debe terminar de configurar. Debido a dificultades técnicas a la hora de automatizar interacciones en la terminal se debe hacer ejecución de los siguientes comandos en la instancia pública (establezca conexión como se muestra en la sección de apache), uno por uno:

```
cd /tmp/asterisk-18.*
sudo contrib/scripts/install_prereq install
sudo ./configure
sudo make menuselect
sudo make -j2
```


> Nota los comandos que realmente daban problemas era contrib/scripts/install_prereq install y sudo make menuselect
>
> Nota 2, en menú select debe elegir las siguientes características:
1. Addons
	- chan_mobile
	- chan_ooh323
	- format_mp3
2. Applications
	- app_macro
3. Core Sound Modules
	- CORE-SOUNDS-EN-WAV
	- CORE-SOUNDS-EN-ULAW
	- CORE-SOUNDS-EN-ALAW
	- CORE-SOUNDS-EN-GSM
	- CORE-SOUNDS-EN-G729
	- CORE-SOUNDS-EN-G722
	- CORE-SOUNDS-EN-SLN16
	- CORE-SOUNDS-EN-SIREN7
4. Enable MOH packages
	- MOH-OPSOUND-WAV
	- MOH-OPSOUND-ALAW
	- MOH-OPSOUND-GSM
5. Enable Extra Sound Packages
	- EXTRA-SOUNDS-EN-WAV
	- EXTRA-SOUNDS-EN-ULAW
	- EXTRA-SOUNDS-EN-ALAW
	- EXTRA-SOUNDS-EN-GSM
	- EXTRA-SOUNDS-EN-G729
	- EXTRA-SOUNDS-EN-G722
	- EXTRA-SOUNDS-EN-SLN16


Una vez ejecutado esto se debe ejecutar el siguiente comando:
```
ansible-playbook -i inventory.ini config_asterisk.yml
```


## Ansible FreePBX
Una vez instalado Asterisk, lo último que resta es la instalación y configuración de FreePBX. Para hacer esto se debe trasladar a la carpeta de “FreePBX-Asterisk” y ejecutar: 


```
ansible-playbook -i inventory.ini install_freePBX.yml
```

Una vez hecho esto, freePBX debería estar instalado y podrá acceder a la administración desde http://<public_instance_public_ip>/admin. Ahora se puede dirigir a la sección [Configurar FreePBX](## Configuración administrativa de FreePBX)

<p align="center">
  <img src="https://media.discordapp.net/attachments/1140393672265965620/1143330959257768058/image.png
" alt="Descripción de la imagen" width="900">
</p>



## Definición de directorios y archivos


* Ansil Apache: Contiene la configuración inicial del servidor público y privado con su respectiva IP de cada uno con el fin de enlazarlos. 
* FreePBX-Asterisk: Contiene los archivos para instalar y configurar Asterisk y FreePBX. 
* Terraform: Contiene una carpeta para definir el compartment a utilizar junto con la estructura del vcn, dominio y las subredes públicas y privadas. 




# Conexión a las Máquinas Virtuales


Utilice los comandos SSH para conectarse a las máquinas virtuales de la siguiente manera:
## Pública 
1. Conexión a la instancia (consola máquina local)
>ssh -i <shh-private-key-path> ubuntu@<public_instance_public_ip>
2. Conexión al server apache (navegador Firefox)
<public_instance_public_ip>


## Privada 
1. Conexión a la instancia
> Conectarse a la instancia publica
> ssh -i /home/ubuntu/opc/vnc_key ubuntu@<private_instance_private_ip>
2. Conexión al server apache (port forwarding)
> (Consola) ssh -i /home/kaled/opc/.oci/vnc_key -L 8080:10.0.1.37:80 ubuntu@150.136.222.57
> (Navegador) localhost:8080


<p align="center">
  <img src="https://media.discordapp.net/attachments/1140393672265965620/1143337769939247244/image.png" alt="Descripción de la imagen" width="900">
</p>

# **Configuración de Servicios**


## Configuración administrativa de FreePBX
- Se debe crear el usuario administrador
- Deben crearse “Extensiones” que son básicamente los usuarios del sistema, estos servirán para que cada usuario pueda iniciar sesión y tenga su propio número para llamarlo


## Inicio de Sesión Zoiper
Para iniciar sesión se debe usar como usuario el formato [extension]@[ip] y usar la clave que se asignó a la hora de crear la extensión


# **Pruebas Realizadas**


Durante la implementación y configuración, se llevaron a cabo diversas pruebas para asegurarnos de que todos los componentes funcionaran correctamente, las siguientes pruebas fueron realizadas para validar el funcionamiento de la infraestructura y las capacidades de llamadas:


## Prueba de Curl Público


Esta prueba tenía como objetivo verificar la conectividad pública del servidor y asegurarse de que estuviera correctamente configurado para recibir solicitudes:


*Se realizó una solicitud Curl al servidor utilizando su dirección IP pública para asegurarse de que el servidor web (Apache) estuviera respondiendo correctamente.


<p align="center">
  <img src="https://cdn.discordapp.com/attachments/1140393672265965620/1143334844101824562/image.png" alt="Descripción de la imagen" width="500">
</p>


## Prueba de Curl Privado


Esta prueba tenía como objetivo verificar la conectividad privada del servidor y asegurarse de que estuviera correctamente configurado dentro de la red privada:


* Se verificó que el HTML funcionara y se aseguró que la comunicación interna estuviera funcionando correctamente

<p align="center">
  <img src="https://cdn.discordapp.com/attachments/1140393672265965620/1143337274227048648/image.png" alt="Descripción de la imagen" width="500">
</p>


## Llamadas


Una de las pruebas más críticas fue la de llamadas telefónicas utilizando Asterisk y FreePBX, estas pruebas tenían como objetivo asegurarse de que el sistema de telefonía funcionara correctamente y permitiera la realización de llamadas.


* Primero se debe de ingresar con un usuario de administrador en freepbx 
* Es importante dejarle su debida extensión para realizar la llamada.
* Con alguna aplicación se puede realizar la llamada, para el caso de nuestro grupo se utilizó Zoiper. 
* Abriendo la aplicación se escribe el usuario siendo la extensión@ip y la contraseña asignada. 
* En el siguiente paso se deja la ip, y luego se da skip. 
* La aplicación escoge la configuración necesaria y ya entra en la pestaña para realizar las llamadas. 
* Las llamadas se realizan a las extensiones, entonces, por eso cada usuario debe de tener una extensión distinta


Estas pruebas fueron esenciales para demostrar que el sistema telefónico estaba funcionando correctamente y era capaz de manejar llamadas telefónicas para ello realizamos las siguientes pruebas:.


* Realizamos diversas llamadas entre los miembros del equipo de manera exitosa

<p align="center">
  <img src="https://cdn.discordapp.com/attachments/1140393672265965620/1143337931407364146/Screenshot_2023-08-21-18-16-30-524_com.zoiper.android.app.jpg.png" alt="Descripción de la imagen" width="200">
</p>

* Se probaron las llamadas usando varias extensiones. Ejemplo de las llamadas de la extensión 2003 a la 2000 y 2006

<p align="center">
  <img src="https://cdn.discordapp.com/attachments/1140393672265965620/1143362653180395550/IMG-20230821-WA0032.jpg" alt="Descripción de la imagen">
</p>

## **Conclusiones**
* Se concluye que utilizar OCI como cloud provider es una manera bastante conveniente de crear la infraestructura de la red para personas con conocimiento básico en redes. Esto es principalmente debido a la gran cantidad de documentación y tutoriales existentes de la misma. 
* Se concluye la importancia de la existencia de Terraform como IaC para automatizar la recreación de la infraestructura de la red.
* Al correr el Script de Terraform y observando cómo este crea un Internet Gateway nos damos cuenta como es de vital importancia para la comunicación de la red pública con el internet. 
* Mediante una Security List, abriendo y cerrando los puertos que se vayan a utilizar para la entrada o salida de los datos, es otra manera de controlar el acceso a las redes.
* El combinar Asterisk y FreePBX para realizar llamadas es una buena manera para entender el funcionamiento de las redes y cómo estas se comunican y transmiten los datos mediante los protocolos.  
* Durante el desarrollo, es probable que surjan desafíos y obstáculos, aprender a identificar y resolver problemas técnicos es una habilidad valiosa que se desarrolla a lo largo del proyecto.
* Este proyecto resulta de gran ayuda como punto de partida para seguir explorando y mejorando los conceptos que vemos a lo largo del curso
* Nos  brindó la oportunidad de familiarizarnos con los temas vistos en clase, como la automatización de infraestructura y la configuración de servicios, esperamos que la experiencia de este proyecto será valiosa en los futuros trabajos.


## **Recomendaciones**
* Seguir los tutoriales al pie de la letra. 
	* Revisar cuales pason están después del actual en la documentación, muchas veces las guías explican el proceso hasta después de hacerlo
* Buscar información externa a las guías si existen dudas.
* Actualizar las guías a versiones actuales.
* Tener el script completo antes de ejecutar cualquier comando. 
* No dejar las entradas y salidas con el puerto 0.0.0.0 para mantener la seguridad.
* Documentar cada paso y decisión de manera detallada
* Antes de realizar un cambio significativo asegurarse de poseer una copia de seguridad o algún método de respaldo en caso de ser necesario revertir los cambios si surge un problema inesperado 
* Realizar diversas pruebas para asegurar que todos los componentes del proyecto son funcionales y funcionan como se espera, esto incluye pruebas de rendimiento y de funcionalidad 
* Revisar el historial de errores






## **Referencias Bibliográficas**
* Oracle, (2023). Terraform: Create a Virtual Cloud Network. Recuperado de [Oracle](https://docs.oracle.com/en-us/iaas/developer-tutorials/tutorials/tf-vcn/01-summary.htm)
* Oracle, (2023). Terraform: Create a Compartment. Recuperado de [Oracle](https://docs.oracle.com/en-us/iaas/developer-tutorials/tutorials/tf-compartment/01-summary.htm)
* Oracle, (2023). Terraform: Set Up OCI Terraform. Recuperado de [Oracle](https://docs.oracle.com/en-us/iaas/developer-tutorials/tutorials/tf-provider/01-summary.htm)
* Cloud Infrastructure Services, (s.f.). How to Install FreePBX on Ubuntu 20.04 (Open Source PBX Tutorial). Recuperado de [Cloud Infrastructure Services](https://cloudinfrastructureservices.co.uk/how-to-install-freepbx-on-ubuntu-20-04/)
* Computing for Geeks, (2018). How To Install FreePBX 16 on Ubuntu 20.04/18.04/16.04. Recuperdo de [Computing for Geeks](https://computingforgeeks.com/how-to-install-with-freepbx-on-ubuntu/?expand_article=1)
* UNIXCOP, (s.f.). Install FreePBX and Asterisk on Ubuntu 22.04. Recuperado de [UNIXCOP](https://unixcop.com/install-freepbx-and-asterisk-on-ubuntu-22-04/)
* freePBX, (2019). Using app_stack.so module instead of app_macro.so in FreePBX. Recuperado de [freePBX](https://community.freepbx.org/t/using-app-stack-so-module-instead-of-app-macro-so-in-freepbx/61927)


