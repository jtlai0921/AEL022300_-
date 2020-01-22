var fun2contrContract = web3.eth.contract([{"constant":false,"inputs":[{"name":"a","type":"fixed128x80[2]"}],"name":"fun2","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"anonymous":false,"inputs":[{"indexed":true,"name":"_from","type":"address"},{"indexed":false,"name":"a","type":"fixed128x80[2]"}],"name":"recEvn","type":"event"}]);
var fun2contr = fun2contrContract.new(
   {
     from: web3.eth.accounts[0], 
     data: '0x608060405234801561001057600080fd5b5061013d806100206000396000f300608060405260043610610041576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063d3275bea14610046575b600080fd5b34801561005257600080fd5b506100966004803603810190808060400190600280602002604051908101604052809291908260026020028082843782019150505050509192919290505050610098565b005b3373ffffffffffffffffffffffffffffffffffffffff167fd11ec24d4545c2cf873f125b4b8371fc4af64e513b5b8da214c3b0cb994f7545826040518082600260200280838360005b838110156100fc5780820151818401526020810190506100e1565b5050505090500191505060405180910390a2505600a165627a7a72305820c63784f96bbc840ba0899f5fa50f9d1c4506a840bffed3181f2d4b827da177cc0029', 
     gas: '4700000'
   }, function (e, contract){
    console.log(e, contract);
    if (typeof contract.address !== 'undefined') {
         console.log('Contract mined! address: ' + contract.address + ' transactionHash: ' + contract.transactionHash);
    }
 })