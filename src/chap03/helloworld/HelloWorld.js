var helloworldContract = web3.eth.contract([{"constant":true,"inputs":[],"name":"greet","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"inputs":[],"payable":false,"stateMutability":"nonpayable","type":"constructor"}]);
var helloworld = helloworldContract.new(
   {
     from: web3.eth.accounts[0], 
     data: '0x60806040526040805190810160405280600b81526020017f68656c6c6f20776f726c640000000000000000000000000000000000000000008152506001908051906020019061004f9291906100a2565b5034801561005c57600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550610147565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100e357805160ff1916838001178555610111565b82800160010185558215610111579182015b828111156101105782518255916020019190600101906100f5565b5b50905061011e9190610122565b5090565b61014491905b80821115610140576000816000905550600101610128565b5090565b90565b610616806101566000396000f300608060405260043610610041576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063cfae321714610046575b600080fd5b34801561005257600080fd5b5061005b6100d6565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561009b578082015181840152602081019050610080565b50505050905090810190601f1680156100c85780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b60606000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614156102435761023c60018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156101c65780601f1061019b576101008083540402835291602001916101c6565b820191906000526020600020905b8154815290600101906020018083116101a957829003601f168201915b50505050506040805190810160405280600181526020017f20000000000000000000000000000000000000000000000000000000000000008152506040805190810160405280600481526020017f626f737300000000000000000000000000000000000000000000000000000000815250610358565b9050610355565b61035260018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102dc5780601f106102b1576101008083540402835291602001916102dc565b820191906000526020600020905b8154815290600101906020018083116102bf57829003601f168201915b50505050506040805190810160405280600181526020017f20000000000000000000000000000000000000000000000000000000000000008152506040805190810160405280600581526020017f6775657374000000000000000000000000000000000000000000000000000000815250610358565b90505b90565b6060806060806060806000808a965089955088945084518651885101016040519080825280601f01601f1916602001820160405280156103a75781602001602082028038833980820191505090505b50935083925060009150600090505b86518110156104695786818151811015156103cd57fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f010000000000000000000000000000000000000000000000000000000000000002838380600101945081518110151561042c57fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a90535080806001019150506103b6565b600090505b855181101561052157858181518110151561048557fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f01000000000000000000000000000000000000000000000000000000000000000283838060010194508151811015156104e457fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a905350808060010191505061046e565b600090505b84518110156105d957848181518110151561053d57fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f010000000000000000000000000000000000000000000000000000000000000002838380600101945081518110151561059c57fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053508080600101915050610526565b8297505050505050505093925050505600a165627a7a7230582034b645fa9a1329dc3cc2244276c5ae4cd9ba1fb850a7acb4bce984df1555d40f0029', 
     gas: '4700000'
   }, function (e, contract){
    console.log(e, contract);
    if (typeof contract.address !== 'undefined') {
         console.log('Contract mined! address: ' + contract.address + ' transactionHash: ' + contract.transactionHash);
    }
 })