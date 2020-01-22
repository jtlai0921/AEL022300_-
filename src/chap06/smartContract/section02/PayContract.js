var paycontractContract = web3.eth.contract([{"constant":true,"inputs":[],"name":"queryBalance","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"recEoa","type":"address"}],"name":"transEth","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"payable":true,"stateMutability":"payable","type":"fallback"}]);
var paycontract = paycontractContract.new(
   {
     from: web3.eth.accounts[0], 
     data: '0x608060405234801561001057600080fd5b50610110806100206000396000f30060806040526004361060485763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166336f40c618114604a578063917f415514606e575b005b348015605557600080fd5b50605c6099565b60408051918252519081900360200190f35b348015607957600080fd5b50604873ffffffffffffffffffffffffffffffffffffffff60043516609e565b303190565b60405173ffffffffffffffffffffffffffffffffffffffff821690303180156108fc02916000818181858888f1935050505015801560e0573d6000803e3d6000fd5b50505600a165627a7a72305820865bbc27a31f22c6b3b86d7181cb8ca7e3b577d1d1063887ea4558043b18deb60029', 
     gas: '4700000'
   }, function (e, contract){
    console.log(e, contract);
    if (typeof contract.address !== 'undefined') {
         console.log('Contract mined! address: ' + contract.address + ' transactionHash: ' + contract.transactionHash);
    }
 })