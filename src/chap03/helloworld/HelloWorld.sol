pragma solidity 0.4.22;

contract HelloWorld {
    
   address owner;
   string greetStr = "hello world";

   constructor() public { 
	 owner = msg.sender; 
   }

    function greet() public constant returns (string) { 
      if (msg.sender == owner) {
	     return strConcat(greetStr, " ", "boss");
	  } else {
	    return strConcat(greetStr, " ", "guest");
	  }
   }
   
   function strConcat(string _a, string _b, string _c) private pure returns (string){
    bytes memory _ba = bytes(_a);
    bytes memory _bb = bytes(_b);
    bytes memory _bc = bytes(_c);
    
    string memory abcde = new string(_ba.length + _bb.length + _bc.length);
    bytes memory babcde = bytes(abcde);
    
    uint k = 0;
    for (uint i = 0; i < _ba.length; i++) babcde[k++] = _ba[i];
    for (i = 0; i < _bb.length; i++) babcde[k++] = _bb[i];
    for (i = 0; i < _bc.length; i++) babcde[k++] = _bc[i];
    
    return string(babcde);
   }
}