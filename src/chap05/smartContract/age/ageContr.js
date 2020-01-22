var agecontrContract = web3.eth.contract([{"constant":true,"inputs":[{"name":"x","type":"uint32"},{"name":"y","type":"bool"}],"name":"isAdult","outputs":[{"name":"r","type":"bool"}],"payable":false,"stateMutability":"pure","type":"function"}]);
var agecontr = agecontrContract.new(
   {
     from: web3.eth.accounts[0], 
     data: '0x608060405234801561001057600080fd5b5060df8061001f6000396000f300608060405260043610603f576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063680a1253146044575b600080fd5b348015604f57600080fd5b50607e600480360381019080803563ffffffff1690602001909291908035151590602001909291905050506098565b604051808215151515815260200191505060405180910390f35b600060148363ffffffff16118060ab5750815b9050929150505600a165627a7a72305820401d45b884d2ef2e32b870ce8a8a05e1b9921baf8bae5891f06df4a9d759a7b50029', 
     gas: '4700000'
   }, function (e, contract){
    console.log(e, contract);
    if (typeof contract.address !== 'undefined') {
         console.log('Contract mined! address: ' + contract.address + ' transactionHash: ' + contract.transactionHash);
    }
 })