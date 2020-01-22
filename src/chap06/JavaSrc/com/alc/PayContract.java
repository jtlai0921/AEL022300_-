package chap06.com.alc;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.5.0.
 */
public class PayContract extends Contract {
    private static final String BINARY = "{\r\n"
            + "\t\"linkReferences\": {},\r\n"
            + "\t\"object\": \"608060405234801561001057600080fd5b50610110806100206000396000f30060806040526004361060485763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166336f40c618114604a578063917f415514606e575b005b348015605557600080fd5b50605c6099565b60408051918252519081900360200190f35b348015607957600080fd5b50604873ffffffffffffffffffffffffffffffffffffffff60043516609e565b303190565b60405173ffffffffffffffffffffffffffffffffffffffff821690303180156108fc02916000818181858888f1935050505015801560e0573d6000803e3d6000fd5b50505600a165627a7a7230582028ae2b585fa2a044965a08309f5789b3b977f2a0f1a6de9c916ede52625e60970029\",\r\n"
            + "\t\"opcodes\": \"PUSH1 0x80 PUSH1 0x40 MSTORE CALLVALUE DUP1 ISZERO PUSH2 0x10 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH2 0x110 DUP1 PUSH2 0x20 PUSH1 0x0 CODECOPY PUSH1 0x0 RETURN STOP PUSH1 0x80 PUSH1 0x40 MSTORE PUSH1 0x4 CALLDATASIZE LT PUSH1 0x48 JUMPI PUSH4 0xFFFFFFFF PUSH29 0x100000000000000000000000000000000000000000000000000000000 PUSH1 0x0 CALLDATALOAD DIV AND PUSH4 0x36F40C61 DUP2 EQ PUSH1 0x4A JUMPI DUP1 PUSH4 0x917F4155 EQ PUSH1 0x6E JUMPI JUMPDEST STOP JUMPDEST CALLVALUE DUP1 ISZERO PUSH1 0x55 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH1 0x5C PUSH1 0x99 JUMP JUMPDEST PUSH1 0x40 DUP1 MLOAD SWAP2 DUP3 MSTORE MLOAD SWAP1 DUP2 SWAP1 SUB PUSH1 0x20 ADD SWAP1 RETURN JUMPDEST CALLVALUE DUP1 ISZERO PUSH1 0x79 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH1 0x48 PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF PUSH1 0x4 CALLDATALOAD AND PUSH1 0x9E JUMP JUMPDEST ADDRESS BALANCE SWAP1 JUMP JUMPDEST PUSH1 0x40 MLOAD PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF DUP3 AND SWAP1 ADDRESS BALANCE DUP1 ISZERO PUSH2 0x8FC MUL SWAP2 PUSH1 0x0 DUP2 DUP2 DUP2 DUP6 DUP9 DUP9 CALL SWAP4 POP POP POP POP ISZERO DUP1 ISZERO PUSH1 0xE0 JUMPI RETURNDATASIZE PUSH1 0x0 DUP1 RETURNDATACOPY RETURNDATASIZE PUSH1 0x0 REVERT JUMPDEST POP POP JUMP STOP LOG1 PUSH6 0x627A7A723058 KECCAK256 0x28 0xae 0x2b PC 0x5f LOG2 LOG0 DIFFICULTY SWAP7 GAS ADDMOD ADDRESS SWAP16 JUMPI DUP10 0xb3 0xb9 PUSH24 0xF2A0F1A6DE9C916EDE52625E609700290000000000000000 \",\r\n"
            + "\t\"sourceMap\": \"28:280:0:-;;;;8:9:-1;5:2;;;30:1;27;20:12;5:2;28:280:0;;;;;;;\"\r\n"
            + "}";

    public static final String FUNC_TRANSETH = "transEth";

    public static final String FUNC_QUERYBALANCE = "queryBalance";

    protected PayContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected PayContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<TransactionReceipt> transEth(String recEoa) {
        final Function function = new Function(
                FUNC_TRANSETH, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(recEoa)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> queryBalance() {
        final Function function = new Function(FUNC_QUERYBALANCE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public static RemoteCall<PayContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(PayContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<PayContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(PayContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static PayContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new PayContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static PayContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new PayContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
