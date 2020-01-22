var timecontractContract = web3.eth.contract([{"constant":true,"inputs":[],"name":"getBlockTime","outputs":[{"name":"t","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"}]);
var timecontract = timecontractContract.new(
   {
     from: web3.eth.accounts[0], 
     data: '0x6080604052348015600f57600080fd5b5060a08061001e6000396000f300608060405260043610603f576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806387ceff09146044575b600080fd5b348015604f57600080fd5b506056606c565b6040518082815260200191505060405180910390f35b6000429050905600a165627a7a72305820324705dba72f410ff4c0169a2bae35261725a2d2c771426c38b62dc0c17b73240029', 
     gas: '4700000'
   }, function (e, contract){
    console.log(e, contract);
    if (typeof contract.address !== 'undefined') {
         console.log('Contract mined! address: ' + contract.address + ' transactionHash: ' + contract.transactionHash);
    }
 })