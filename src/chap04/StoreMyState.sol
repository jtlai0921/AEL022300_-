pragma solidity ^0.4.22;

/** @title Store My State  */
contract StoreMyState {
  
  uint myState;
  
  /** @dev To store a state value
    * @param var value to store in contract.
    */
  function set(uint var) public {
    myState= var;
  }

  /** @dev To query state value.
    * @return v The state value in contract.
    */
  function get() public view returns (uint v) {
   return myState;
  }
}
