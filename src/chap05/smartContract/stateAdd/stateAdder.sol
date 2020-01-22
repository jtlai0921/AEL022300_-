pragma solidity 0.4.24;

contract stateAdder {
  
  uint myInt = 0;
  
  function appendAdd(uint in01) public returns (uint) {
   myInt += in01;
   return myInt;
  }
}