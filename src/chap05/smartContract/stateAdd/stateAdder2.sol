pragma solidity 0.4.24;

contract stateAdder2 {
  
  uint public myInt = 0;
  
  event Rtnvalue(address indexed _from, uint myInt);
   
  function appendAdd(uint in01) public returns (uint) {
   myInt += in01;
   emit Rtnvalue(msg.sender, myInt);
   return myInt;
  }
}