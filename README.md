# vro-plugin-basicmachine

## Functionality
Configurations/Resources will stored persistent ✔ <br/>
Configurations/Resources will cached by the plugin for better performance ✔ <br/>
Configurations/Resources show up in the plugin inventory ✔ <br/>
Workflow included for save, delete and update BasicMachine's ✔ <br/>

### BasicMachine:BasicMachine (*Type*)

#### BasicMachine (ScriptingObject)

##### Properties
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
* *saveBasicMachine(name, ipAddress, dnsName, cpu, memory, operatingSystem, diskSize, powerState, snapshot, initialUsername, initialPassword, description, json)*
* *deleteBasicMachine(id)*
* *getBasicMachineByID(id)*

Note: All properties can also be accessed and modified directly!

Example:
```javascript
var bm = BasicMachineManager.getBasicMachineById(id);

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
