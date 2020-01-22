var stateadder2Contract = web3.eth.contract([{"constant":false,"inputs":[{"name":"in01","type":"uint256"}],"name":"appendAdd","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"myInt","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"anonymous":false,"inputs":[{"indexed":true,"name":"_from","type":"address"},{"indexed":false,"name":"myInt","type":"uint256"}],"name":"Rtnvalue","type":"event"}]);
var stateadder2 = stateadder2Contract.new(
   {
     from: web3.eth.accounts[0], 
     data: '0x60806040526000805534801561001457600080fd5b5061013d806100246000396000f30060806040526004361061004c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630b6eb9ce146100515780632cb777a61461007e575b600080fd5b34801561005d57600080fd5b5061007c600480360381019080803590602001909291905050506100a9565b005b34801561008a57600080fd5b5061009361010b565b6040518082815260200191505060405180910390f35b8060008082825401925050819055503373ffffffffffffffffffffffffffffffffffffffff167fd00b1aba82025d64555bd19e01ee7b4d7e20e6aa2610dc4774cd604e0fe79d5d6000546040518082815260200191505060405180910390a250565b600054815600a165627a7a723058207c455cce612f241819153b2cf7b2afb848b715244e0c690f529ee6a6eec24cbf0029', 
     gas: '4700000'
   }, function (e, contract){
    console.log(e, contract);
    if (typeof contract.address !== 'undefined') {
         console.log('Contract mined! address: ' + contract.address + ' transactionHash: ' + contract.transactionHash);
    }
 })