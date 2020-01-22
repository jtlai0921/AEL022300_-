pragma solidity 0.4.25;

contract KYC { 
 
 //自訂的資料結構  
 struct customer {
  string name; //姓名
  uint8  age;   //年齡
 }
 
 //映射EOA與資料 
 mapping(uint => customer) private customers;
 
 //將資訊記錄在Log  
 event InsertEvn(address indexed _from, uint id, string name);
 
 //新增客戶
 function doInsert(uint id, string name, uint8 age) public {  
  customers[id].name = name;  
  customers[id].age = age;
  emit InsertEvn(msg.sender, id, name);
 }
 
 function queryName(uint id) public view returns (string) {
  return customers[id].name;
 }

 function queryAge(uint id) public view returns (uint8) {
  return customers[id].age;
 } 
}
