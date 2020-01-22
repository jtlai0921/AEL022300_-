pragma solidity 0.4.22;

contract Multiply {
   
  function doMultiply(uint in01, uint in02) public constant returns (uint) {
   return in01 * in02;
  }
}