package com.tstudioz.fax.fme.feature.iksica.repository

import com.tstudioz.fax.fme.feature.iksica.dao.IksicaDao
import com.tstudioz.fax.fme.feature.iksica.models.IksicaData
import com.tstudioz.fax.fme.feature.iksica.models.IksicaResult
import com.tstudioz.fax.fme.feature.iksica.models.Receipt
import com.tstudioz.fax.fme.feature.iksica.models.ReceiptRoom
import com.tstudioz.fax.fme.feature.iksica.models.StudentData
import com.tstudioz.fax.fme.feature.iksica.models.StudentDataRoom
import com.tstudioz.fax.fme.util.receiptsTestData
import com.tstudioz.fax.fme.util.singleReceiptData
import com.tstudioz.fax.fme.util.studentDataTestData

class MockIksicaRepository(
    private val iksicaDao: IksicaDao,
) : IksicaRepositoryInterface {

    override suspend fun getCardDataAndReceipts(): IksicaResult.CardAndReceiptsResult {
        val studentInfo = StudentData(studentDataTestData)

        val receipts = receiptsTestData.map { Receipt(it) }
        insert(receipts)
        insert(studentInfo)

        return IksicaResult.CardAndReceiptsResult.Success(IksicaData(studentInfo, receipts))
    }

    override suspend fun getReceipt(url: String): IksicaResult.ReceiptResult {
        return IksicaResult.ReceiptResult.Success(singleReceiptData.toMutableList())
    }

    override suspend fun insert(model: StudentData) {
        iksicaDao.insert(StudentDataRoom(model))
    }

    override suspend fun insert(model: List<Receipt>) {
        iksicaDao.deleteAll()
        iksicaDao.insert(model.map { ReceiptRoom(it) })
    }

    override suspend fun getCache(): IksicaData? {
        val model = iksicaDao.readData() ?: return null

        val receipts = iksicaDao.readReceipts()?.map { Receipt(it) }

        return IksicaData(StudentData(model), receipts)
    }
}
