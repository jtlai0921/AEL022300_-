var stateadderContract = web3.eth.contract([{"constant":false,"inputs":[{"name":"in01","type":"uint256"}],"name":"appendAdd","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"nonpayable","type":"function"}]);
var stateadder = stateadderContract.new(
   {
     from: web3.eth.accounts[0], 
     data: '0x60806040526000805534801561001457600080fd5b5060c9806100236000396000f300608060405260043610603f576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630b6eb9ce146044575b600080fd5b348015604f57600080fd5b50606c600480360381019080803590602001909291905050506082565b6040518082815260200191505060405180910390f35b600081600080828254019250508190555060005490509190505600a165627a7a72305820c6feabbda68cca8eb4a74570bf3a20250a295b79505b2ea7a695d861e60bc0b20029', 
     gas: '4700000'
   }, function (e, contract){
    console.log(e, contract);
    if (typeof contract.address !== 'undefined') {
         console.log('Contract mined! address: ' + contract.address + ' transactionHash: ' + contract.transactionHash);
    }
 })