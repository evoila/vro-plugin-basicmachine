# vro-plugin-basicmachine

The BasicMachine plugin allows persistent storage of information about virtual machines as resources within the VMware vRealize Orchestrator. The stored resources of the type BasicMachine are displayed graphically in the plugin inventory.
The so-called class BasicMachineManager provides methods to create, update, and remove these resources. It also offers several methods for querying and filtering.

#### Plugin was tested under <br/>
vRealize Automation 7.5.0, vRealize Automation 7.4.0, vRealize Automation 7.3.1  and vRealize Orchestrator 7.6.0 ✅

## Functionality
Configurations/Resources will stored persistent ✔ <br/>
Configurations/Resources will cached by the plugin for better performance ✔ <br/>
Configurations/Resources show up in the plugin inventory ✔ <br/>
Workflow included for save, delete and update BasicMachine's ✔ <br/>

### BasicMachine:BasicMachine (*Type*)

#### BasicMachine (ScriptingObject)

##### Properties
* owner (*String*)
* name (*String*)
* ipAddress (*String*)
* dnsName (*String*)
* cpu (*String*)
* memory (*String*)
* operatingSystem (*String*)
* diskSize (*String*)
* powerState (*String*)
* snapshot (*String*)
* initialUsername (*String*)
* initialPassword (*String*)
* description (*String*)
* json (*String*)

#### BasicMachineManager (*Singleton*)

##### Methods
* *saveBasicMachine(owner, name, ipAddress, dnsName, cpu, memory, operatingSystem, diskSize, powerState, snapshot, initialUsername, initialPassword, description, json)*
* *deleteBasicMachine(id)*
* *getBasicMachineByID(id)*
* *allBasicMachines()*
* *getBasicMachinesByOwner(owner)*
* *getBasicMachinesByName(name)*
* *getBasicMachinesByOperatingSystem(operatingSystem)*
* *getBasicMachinesByDnsName(dnsName)*
* *getBasicMachinesByIpAddress(ipAddress)*

Note: All properties can also be accessed and modified directly!

Example:
```javascript
var bm = BasicMachineManager.getBasicMachineById(id);

bm.owner = "johndoe"
bm.name = "machineA";
bm.ipAddress = "0.0.0.0";
bm.dnsName = "machineA";
bm.cpu = "2";
bm.memory = "1024";
bm.operatingSystem = "linux";
bm.diskSize = "3096"";
bm.powerState = "0";
bm.snapshot = "0";
bm.initialUsername = "admin";
bm.initialPassword = "password";
bm.description = "this is an example";
bm.json = "json string goes here";
```

## Screenhots

### API Explorer
<img src="https://github.com/latzinger/vro-plugin-basicmachine/blob/master/images/apiexplorer.png" alt="API-Explorer"/>

### Plugin Inventory
<img src="https://github.com/latzinger/vro-plugin-basicmachine/blob/master/images/inventory.png" alt="Inventory"/>
