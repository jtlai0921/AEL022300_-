var multiplyContract = web3.eth.contract([{"constant":true,"inputs":[{"name":"in01","type":"uint256"},{"name":"in02","type":"uint256"}],"name":"doMultiply","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"}]);
var multiply = multiplyContract.new(
   {
     from: web3.eth.accounts[0], 
     data: '0x608060405234801561001057600080fd5b5060c58061001f6000396000f300608060405260043610603f576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063648146a2146044575b600080fd5b348015604f57600080fd5b5060766004803603810190808035906020019092919080359060200190929190505050608c565b6040518082815260200191505060405180910390f35b60008183029050929150505600a165627a7a723058203b839f390dbccb99f5903002bbc0d82039b5bba59b954e754e71f84828b16cf60029', 
     gas: '4700000'
   }, function (e, contract){
    console.log(e, contract);
    if (typeof contract.address !== 'undefined') {
         console.log('Contract mined! address: ' + contract.address + ' transactionHash: ' + contract.transactionHash);
    }
 })