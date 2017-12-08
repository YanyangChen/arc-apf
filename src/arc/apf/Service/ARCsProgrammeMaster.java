package arc.apf.Service;
//package acf.acf.Service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acf.acf.Abstract.ACFaAppService;
import acf.acf.Abstract.ACFaSQLAss;
import acf.acf.Dao.ACFoModule;
import acf.acf.Dao.ACFoModuleOwner;
import acf.acf.Database.ACFdSQLAssDelete;
import acf.acf.Database.ACFdSQLAssInsert;
import acf.acf.Database.ACFdSQLAssSelect;
import acf.acf.Database.ACFdSQLAssUpdate;
import acf.acf.General.core.ACFgRawModel;
import acf.acf.Interface.ACFiCallback;
import acf.acf.Interface.ACFiSQLAssInterface;
import acf.acf.Interface.ACFiSQLAssWriteInterface;
import acf.acf.Model.ACFmModule;
import acf.acf.Static.ACFtDBUtility;
import acf.acf.Static.ACFtUtility;
import arc.apf.Model.ARCmProgrammeMaster;

@Service
public abstract class ARCsProgrammeMaster extends ACFaAppService{
    
    public ARCsProgrammeMaster() throws Exception {
        super();
        // TODO Auto-generated constructor stub
    }

   
    public abstract List<ACFgRawModel> getProgrammeNo() throws Exception; //for apff011 combobox, AC 2017/03/20
    public abstract List<ACFgRawModel> getBusinessPlatform() throws Exception;//for apff011 combobox, AC 2017/03/20
    public abstract List<ACFgRawModel> getDepartment() throws Exception;//for apff011 combobox, AC 2017/03/20
    public abstract List<ACFgRawModel> getBBusinessPlatform() throws Exception;//for apff011 combobox, AC 2017/03/20
    public abstract List<ACFgRawModel> getBDepartment() throws Exception;//for apff011 combobox, AC 2017/03/20
    public abstract List<ACFgRawModel> getProgNoPairs() throws Exception;
    public abstract List<ACFgRawModel> getProgNamePairs() throws Exception;
    public abstract Timestamp UpdateFirstInput(String programme_no) throws Exception;
    public abstract List<ACFgRawModel> getToProgrammeFields(String programme_no) throws Exception;
    public abstract List<ACFgRawModel> getNewToProgrammeFields(String programme_no) throws Exception;
    public abstract String getProgrammeName(String programme_no) throws Exception; //for apff011 ajax retrieve, AC 2017/03/20
    public abstract String getProgrammePlatform(String programme_no) throws Exception; //for apff011 ajax retrieve, AC 2017/03/20
    public abstract String getProgrammeDepartment(String programme_no) throws Exception; //for apff011 ajax retrieve, AC 2017/03/20
    public abstract String getBusinessPlatformDesc(String programme_no) throws Exception; //sf
    public abstract Timestamp getCost1stInputDate(String programme_no) throws Exception; // for labour consumption checking,CN,20170727
    public abstract void updateCost1stInputDate(ARCmProgrammeMaster before, Timestamp cost_1st_input_date) throws Exception;  // for labour consumption updating,CN,20170727
   	public abstract boolean isProgNoExisted(String programme_no) throws Exception;  // for apcf001, apcf002,SF,2017/08/14
   	public abstract void updateFromTransferStatus(ARCmProgrammeMaster before, String trf_status, String trf_programme, Timestamp trf_date, String trf_remarks) throws Exception;  // for programme transfer,CN,20171017
   	public abstract void updateToTransferStatus(ARCmProgrammeMaster before, String trf_status, String trf_programme, Timestamp trf_date, String trf_remarks) throws Exception;  // for programme transfer,CN,20171017
   	
}