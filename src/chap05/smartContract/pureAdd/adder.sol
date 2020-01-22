pragma solidity 0.4.24;

contract Adder {
  function doAdd(uint in01, uint in02) public pure returns (uint) {
   return in01 + in02;
  }
}