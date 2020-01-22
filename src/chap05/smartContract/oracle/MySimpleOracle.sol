pragma solidity 0.4.25; 
  
contract MySimpleOracle { 
     
  event MyEvent(uint256 indexed _id, string _myMsg); 
      
  function myfunc(uint256 id, string myMsg) public { 
   emit MyEvent(id, myMsg); 
  }
}