pragma solidity ^0.4.25;

contract InsuranceContract {
    
 //醫院EOA
 address private hospital;
 
 //保險公司EOA
 address private insuranceCorp;
 
 //病歷資訊
 struct MedicalRecord {
  string symptom;	//症狀
  string cause;		//病因
  uint day;		//住院天數
  uint money;		//住院花費
  bool exist;
 }
 
 //記錄病人資訊
 struct Patient {
  string name;	//姓名
  string addr;	//住家地址
  uint recordCnt; //病歷總量
  mapping(uint => MedicalRecord) records; //病歷
  bool exist;
 }
 
 //儲存所有病人基本資訊
 mapping(address => Patient) private patientData;
 
 //記錄合約主持人(保險公司)
 constructor () public {
   insuranceCorp = msg.sender;
 }
 	
 //只有保險公司可執行 
 modifier onlyInsuranceCorp() {
   require(msg.sender == insuranceCorp,
   "only insuranceCorp can do this");
    _;
 }
 
 //只有醫院可執行 
 modifier onlyHospital() {
   require(msg.sender == hospital,
   "only hospital can do this");
    _;
 }
  
 //只有醫院和保險公司可執行 
 modifier onlyHospitalAndInsuranceCorp() {
   require(msg.sender == hospital || msg.sender == insuranceCorp,
   "only hospital and insuranceCorp can do this");
    _;
 }
  
 //設定醫院EOA
 function setHospital(address _hospital) public onlyInsuranceCorp {
  hospital = _hospital;
 }
	
 //查詢醫院位址  
 function getHospital() public view returns(address) {
  return hospital;
 }
 
 //新增一筆病人資訊
 function insPatient(address patientAddr, string name,string addr) public onlyHospital {
  require(!isPatientExist(patientAddr), 
		 "patient data already exist");
		 
   patientData[patientAddr].name = name; 
   patientData[patientAddr].addr = addr;
   patientData[patientAddr].recordCnt = 0;
   patientData[patientAddr].exist = true;
   
   emit InsPatientEvnt("insPatient", patientAddr);
 }
 
 //查詢病人資訊是否存在
 function isPatientExist(address patientAddr) public view returns(bool) {
   return patientData[patientAddr].exist;
 }
 
 //新增病人事件
 event InsPatientEvnt(string indexed eventType, address patientAddr);
 
 //新增一筆離院申請
 function insRecord(address patientAddr, string symptom, string cause, uint day, uint money) public onlyHospital returns(uint){
   require(isPatientExist(patientAddr), 
		 "patient data not exist");
   
   //病歷序號加1   
   patientData[patientAddr].recordCnt+=1;
   uint inx = patientData[patientAddr].recordCnt;
   
   //新離院資訊
   MedicalRecord memory record = MedicalRecord({
	 symptom: symptom,	//症狀
	 cause: cause,		//病因
	 day: day,			//住院天數
	 money: money,		//住院花費	 
	 exist: true		//確認資訊存在
   });
	
   //新增病歷於病人記錄
   patientData[patientAddr].records[inx] = record;
	
   //觸發離院事件
   emit InsRecordEvnt("insRecord", patientAddr, inx, day, money);
   
   //回傳病歷序號
   return inx; 
 }
 
 //離院申請事件
 event InsRecordEvnt(string indexed eventType, address patientAddr, uint recordID, uint day, uint money);
 
 //查詢離院申請資訊-病因
 function queryRecordCause(address patientAddr, uint recordID) public onlyHospitalAndInsuranceCorp view returns(string){
   require(isPatientExist(patientAddr), 
		 "patient data not exist");

   require(patientData[patientAddr].records[recordID].exist, 
		 "medical record not exist");
   	
   return patientData[patientAddr].records[recordID].symptom;
 }
 
 //查詢離院申請資訊-住院天數
 function queryRecordDays(address patientAddr, uint recordID) public onlyHospitalAndInsuranceCorp view returns(uint){
   require(isPatientExist(patientAddr), 
		 "patient data not exist");

   require(patientData[patientAddr].records[recordID].exist, 
		 "medical record not exist");
   	
   return patientData[patientAddr].records[recordID].day;
 }
 
 //查詢離院申請資訊-住院費用
 function queryRecordMoney(address patientAddr, uint recordID) public onlyHospitalAndInsuranceCorp view returns(uint){
   require(isPatientExist(patientAddr), 
		 "patient data not exist");

   require(patientData[patientAddr].records[recordID].exist, 
		 "medical record not exist");
   	
   return patientData[patientAddr].records[recordID].money;
 }
}
