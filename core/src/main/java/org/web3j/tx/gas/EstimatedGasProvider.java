/*
 * Copyright 2019 Web3 Labs LTD.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.tx.gas;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.Transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class EstimatedGasProvider extends StaticGasProvider {

    private final Web3j web3j;

    public EstimatedGasProvider(Web3j web3j, BigInteger fallbackGasPrice, BigInteger fallbackGasLimit) {
        super(fallbackGasPrice, fallbackGasLimit);
        this.web3j = web3j;
    }

    @Override
    public BigInteger getGasPrice(String contractFunc) {
        return getGasPrice();
    }

    @Override
    public BigInteger getGasPrice() {
        try {
            return web3j.ethGasPrice().send().getGasPrice();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.getGasPrice();
    }

    @Override
    public BigInteger getGasLimit(String contractFunc) {
        return super.getGasLimit(contractFunc);
    }

    @Override
    public BigInteger getGasLimit() {
        return super.getGasLimit();
    }

    @Override
    public BigInteger getGasLimit(String fromAddress, String contractAddress, String data, BigInteger weiValue, BigInteger gasPrice) {
        try {
            return web3j.ethEstimateGas(
                    Transaction.createFunctionCallTransaction(
                            fromAddress,
                            null,
                            gasPrice,
                            null,
                            contractAddress,
                            weiValue,
                            data))
                    .sendAsync()
                    .get().getAmountUsed();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return super.getGasLimit();
    }
}
