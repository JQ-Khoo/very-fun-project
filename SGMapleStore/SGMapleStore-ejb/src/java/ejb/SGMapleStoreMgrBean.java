package ejb;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import entity.EmployeeEntity;
import entity.ContactEntity;
import entity.ItemEntity;
import entity.InventoryLogEntity;
import entity.CompositeItemEntity;
import entity.InvoiceEntity;
import entity.CategoryEntity;
import entity.SalesOrderEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

@Stateless
public class SGMapleStoreMgrBean implements CommonInfrastructureRemote, WarehouseTransportRemote {
    @PersistenceContext
    private EntityManager em;
    
    private EmployeeEntity eEntity;
    private ContactEntity cEntity;
    private InventoryLogEntity ilEntity;
    private CompositeItemEntity ciEntity;
    private ItemEntity iEntity;
    private InvoiceEntity invoice;
    
    @Override
    public boolean createContact(String contactSalutation, String contactFirstName, String contactLastName, String contactEmail, 
            String contactPhone, String contactType, String contactBillingAttn, String contactBillingAddress, String contactBillingCity, 
            String contactBillingState, String contactBillingZipCode, String contactBillingCountry, String contactBillingFax, 
            String contactBillingPhone, String contactShippingAttn, String contactShippingAddress, String contactShippingCity, 
            String contactShippingState, String contactShippingZipCode, String contactShippingCountry, String contactShippingFax, 
            String contactShippingPhone, String contactUsername, String contactPassword, String suppCompanyName, String suppBillAccNo, 
            String contactNotes) {
        String hashedPassword = "";
        try{ hashedPassword = encodePassword(contactPassword); }
        catch(NoSuchAlgorithmException ex){ ex.printStackTrace(); }
        
        cEntity = new ContactEntity();
        cEntity.createContact(contactSalutation, contactFirstName, contactLastName, contactEmail, contactPhone, contactType, 
                contactBillingAttn, contactBillingAddress, contactBillingCity, contactBillingState, contactBillingZipCode, 
                contactBillingCountry, contactBillingFax, contactBillingPhone, contactShippingAttn, contactShippingAddress, 
                contactShippingCity, contactShippingState, contactShippingZipCode, contactShippingCountry, contactShippingFax, 
                contactShippingPhone, contactUsername, hashedPassword, suppCompanyName, suppBillAccNo, contactNotes);
        em.persist(cEntity);
        return true;
    }
    
    @Override
    public List<Vector> viewContactList(){
        Query q = em.createQuery("SELECT c FROM Contact c");
        List<Vector> contactList = new ArrayList<Vector>();
        
        for(Object o: q.getResultList()){
            ContactEntity contactE = (ContactEntity) o;
            Vector contactVec = new Vector();
            
            contactVec.add(contactE.getContactSalutation());
            contactVec.add(contactE.getContactFirstName());
            contactVec.add(contactE.getContactLastName());
            contactVec.add(contactE.getContactEmail());
            contactVec.add(contactE.getContactPhone());
            contactVec.add(contactE.getContactType());
            contactVec.add(contactE.getSuppCompanyName());
            contactVec.add(contactE.getContactActiveStatus());
            contactList.add(contactVec);
        }
        return contactList;
    }
    
    @Override
    public Vector getContactInfo(String contactIdentifier) {
        cEntity = lookupContact(contactIdentifier);
        Vector contactInfoVec = new Vector();
        
        if (cEntity != null) {
            DateFormat df = new SimpleDateFormat("dd MMMMM yyyy");
            
            contactInfoVec.add(cEntity.getContactFirstName());
            contactInfoVec.add(cEntity.getContactLastName());
            contactInfoVec.add(cEntity.getContactEmail());
            contactInfoVec.add(cEntity.getContactActiveStatus());
            contactInfoVec.add(df.format(cEntity.getContactCreationDate()));
            
            return contactInfoVec;
        }
        return null;
    }
    
    @Override
    public boolean deactivateMultipleContact(String[] contactEmailListArr) {
        boolean contactDeletionStatus = true;
        for (String contactEmail : contactEmailListArr) {
            if (lookupContact(contactEmail) == null) {
                contactDeletionStatus = false;
            } else {
                cEntity = lookupContact(contactEmail);
                cEntity.setContactActiveStatus(false);
                em.merge(cEntity);
            }
        }
        return contactDeletionStatus;
    }
    
    @Override
    public boolean deactivateAContact(String hiddenContactEmail) {
        boolean contactDeletionStatus = true;
        if (lookupContact(hiddenContactEmail) == null) {
            contactDeletionStatus = false;
        } else {
            cEntity = lookupContact(hiddenContactEmail);
            cEntity.setContactActiveStatus(false);
            em.merge(cEntity);
        }
        return contactDeletionStatus;
    }
    
    @Override
    public boolean activateAContact(String hiddenContactEmail) {
        boolean contactDeletionStatus = true;
        if (lookupContact(hiddenContactEmail) == null) {
            contactDeletionStatus = false;
        } else {
            cEntity = lookupContact(hiddenContactEmail);
            cEntity.setContactActiveStatus(true);
            em.merge(cEntity);
        }
        return contactDeletionStatus;
    }
    
    @Override
    public boolean createEmployee(String empSalutation, String empFirstName, String empLastName, String empEmail, 
            String empPhone, String empUniqueIdentifier, String empDateOfBirth, String empGender, String empRace, 
            String empNationality, String empResidentAddress, String empResidentCity, String empResidentState, 
            String empResidentZipCode, String empResidentCountry, String empJobDepartment, String empJobDesignation, 
            String empUsername, String empPassword, String empNotes) {
        String hashedPassword = "";
        try{ hashedPassword = encodePassword(empPassword); }
        catch(NoSuchAlgorithmException ex){ ex.printStackTrace(); }
        
        eEntity = new EmployeeEntity();
        eEntity.createEmployee(empSalutation, empFirstName, empLastName, empEmail, empPhone, empUniqueIdentifier, empDateOfBirth, 
                empGender, empRace, empNationality, empResidentAddress, empResidentCity, empResidentState, empResidentZipCode, 
                empResidentCountry, empJobDepartment, empJobDesignation, empUsername, hashedPassword, empNotes);
        em.persist(eEntity);
        return true;
    }
    
    @Override
    public List<Vector> viewEmployeeList(){
        Query q = em.createQuery("SELECT e FROM Employee e");
        List<Vector> employeeList = new ArrayList<Vector>();
        
        for(Object o: q.getResultList()){
            EmployeeEntity employeeE = (EmployeeEntity) o;
            Vector employeeVec = new Vector();
            
            employeeVec.add(employeeE.getEmpFirstName());
            employeeVec.add(employeeE.getEmpLastName());
            employeeVec.add(employeeE.getEmpEmail());
            employeeVec.add(employeeE.getEmpPhone());
            employeeVec.add(employeeE.getEmpJobDepartment());
            employeeVec.add(employeeE.getEmpJobDesignation());
            employeeVec.add(employeeE.getEmpActiveStatus());
            employeeList.add(employeeVec);
        }
        return employeeList;
    }
    
    @Override
    public Vector getEmployeeInfo(String employeeIdentifier) {
        eEntity = lookupEmployee(employeeIdentifier);
        Vector employeeInfoVec = new Vector();
        
        if (eEntity != null) {
            DateFormat df = new SimpleDateFormat("dd MMMMM yyyy");
            
            employeeInfoVec.add(eEntity.getEmpFirstName());
            employeeInfoVec.add(eEntity.getEmpLastName());
            employeeInfoVec.add(eEntity.getEmpEmail());
            employeeInfoVec.add(eEntity.getEmpActiveStatus());
            employeeInfoVec.add(df.format(eEntity.getEmpCreationDate()));
            
            return employeeInfoVec;
        }
        return null;
    }
    
    @Override
    public boolean deactivateMultipleEmployee(String[] empEmailListArr) {
        boolean empDeletionStatus = true;
        for (String empEmail : empEmailListArr) {
            if (lookupEmployee(empEmail) == null) {
                empDeletionStatus = false;
            } else {
                eEntity = lookupEmployee(empEmail);
                eEntity.setEmpActiveStatus(false);
                em.merge(eEntity);
            }
        }
        return empDeletionStatus;
    }
    
    @Override
    public boolean deactivateAnEmployee(String hiddenEmpEmail) {
        boolean empDeletionStatus = true;
        if (lookupEmployee(hiddenEmpEmail) == null) {
            empDeletionStatus = false;
        } else {
            eEntity = lookupEmployee(hiddenEmpEmail);
            eEntity.setEmpActiveStatus(false);
            em.merge(eEntity);
        }
        return empDeletionStatus;
    }
    
    @Override
    public boolean activateAnEmployee(String hiddenEmpEmail) {
        boolean empDeletionStatus = true;
        if (lookupEmployee(hiddenEmpEmail) == null) {
            empDeletionStatus = false;
        } else {
            eEntity = lookupEmployee(hiddenEmpEmail);
            eEntity.setEmpActiveStatus(true);
            em.merge(eEntity);
        }
        return empDeletionStatus;
    }
    
    /* WAREHOUSE-TRANSPORT MODULE (JSON) */
    @Override
    public List<Vector> getItemListingNames() {
        Query q = em.createQuery("SELECT i FROM Item i");
        
        List<Vector> itemList = new ArrayList();
        for(Object o: q.getResultList()){
            ItemEntity ie = (ItemEntity) o;
            Vector itemVec = new Vector();
            
            itemVec.add(ie.getItemName());
            itemVec.add(ie.getItemSKU());
            itemVec.add(ie.getItemQuantity());
            itemVec.add(ie.getItemSellingPrice());
            itemList.add(itemVec);
        }
        return itemList;
    }

    @Override
    public boolean createInventoryLog(String userNRIC, String logDate, String logReason, String logDescription, 
            String[] itemNameArr, String[] itemSKUArr, String[] itemQtyArr, String[] itemQtyAdjustArr) {
        boolean logCreationStatus = true;
        Double itemQtyBeforeAdjust = 0.0;
        Double itemQtyAfterAdjust = 0.0;
        
        for(int i = 0; i < itemNameArr.length; i++) {
            ilEntity = new InventoryLogEntity();
            itemQtyBeforeAdjust = Double.parseDouble(itemQtyArr[i]);
            String qtyOperator = itemQtyAdjustArr[i].substring(0, 1);
            String qtyAdjustValue = itemQtyAdjustArr[i].substring(1, itemQtyAdjustArr[i].length());
            if(qtyOperator.equals("+")) {
                itemQtyAfterAdjust = itemQtyBeforeAdjust + Double.parseDouble(qtyAdjustValue);
                ilEntity.createInventoryLog(userNRIC, logDate, logReason, logDescription, itemNameArr[i], itemSKUArr[i], 
                    itemQtyBeforeAdjust, itemQtyAfterAdjust, itemQtyAdjustArr[i]);
                em.persist(ilEntity);
            }
            else if(qtyOperator.equals("-") && Double.parseDouble(qtyAdjustValue) > 0) {
                itemQtyAfterAdjust = itemQtyBeforeAdjust - Double.parseDouble(qtyAdjustValue);
                ilEntity.createInventoryLog(userNRIC, logDate, logReason, logDescription, itemNameArr[i], itemSKUArr[i], 
                    itemQtyBeforeAdjust, itemQtyAfterAdjust, itemQtyAdjustArr[i]);
                em.persist(ilEntity);
            }
            else {
                logCreationStatus = false;
            }
        }
        return logCreationStatus;
    }
    
    @Override
    public List<Vector> viewInventoryLogList(){
        Query q = em.createQuery("SELECT l FROM InventoryLog l");
        List<Vector> logList = new ArrayList<Vector>();
        
        for(Object o: q.getResultList()){
            InventoryLogEntity logE = (InventoryLogEntity) o;
            Vector logVec = new Vector();
            
            logVec.add(logE.getLogDate());
            logVec.add(logE.getLogReason());
            logVec.add(logE.getLogCreatorID());
            logVec.add(logE.getItemName());
            logVec.add(logE.getItemSKU());
            logVec.add(logE.getItemQtyAdjustValue());
            logList.add(logVec);
        }
        return logList;
    }
    
    public boolean createInventoryCategory(String newCategoryName,String newCategoryDesc,ArrayList<String> sCats){
        CategoryEntity newInventoryCategory = new CategoryEntity();
        newInventoryCategory.setName(newCategoryName);
        newInventoryCategory.setDescription(newCategoryDesc);
        newInventoryCategory.setSubcategories(sCats);
        newInventoryCategory.setActive(true);
        em.persist(newInventoryCategory);
        return true;
    }
    
    
    public List<Vector> viewAllInventoryCategories(){
        Query q = em.createQuery("SELECT c FROM Category c");
        List results = q.getResultList();
        List<Vector> categories = new ArrayList<Vector>();
        
        for(Object o:q.getResultList()){
            CategoryEntity cate = (CategoryEntity) o;
            Vector cateVec = new Vector();
            cateVec.add(cate.getName());
            cateVec.add(cate.getDescription());
            ArrayList<String> subs = cate.getSubcategories();
            String subsline = "";
            for(int n=0;n<subs.size();n++){
                if(n==0){
                    subsline = subsline+subs.get(n);
                }else{
                    subsline = subsline+";"+subs.get(n);
                }
            }
            cateVec.add(subsline);
            categories.add(cateVec);
        }
        return categories;
    }
    
    public void modifyInventoryCategory(String categoryName,String updatedCategoryDesc,ArrayList<String> sCats){
        Query q = em.createQuery("SELECT c FROM Category c WHERE c.name=:cateName");
        q.setParameter("cateName", categoryName);
        CategoryEntity categoryE = (CategoryEntity)q.getSingleResult();
        categoryE.setDescription(updatedCategoryDesc);
        categoryE.setSubcategories(sCats);
        em.persist(categoryE);
    }
    
    @Override
    public boolean createCompositeItem(String compositeName, String compositeSKU, String compositeSellPrice, 
            String compositeRebundleLvl, String compositeDescription, String fileName, String[] itemNameArr, String[] itemSKUArr, 
            String[] itemQtyRequiredArr) {
        ciEntity = new CompositeItemEntity();
        Vector itemInventotySKUVec = new Vector();
        
        List<Vector> packageItemList = new ArrayList<Vector>();
        for(int i = 0; i < itemNameArr.length; i++) {
            Vector packageItemVec = new Vector();
            packageItemVec.add(itemNameArr[i]);
            packageItemVec.add(itemSKUArr[i]);
            itemInventotySKUVec.add(itemSKUArr[i]);
            packageItemVec.add(itemQtyRequiredArr[i]);
            packageItemList.add(packageItemVec);
        }
        ciEntity.createCompositeItem(compositeName, compositeSKU, Double.parseDouble(compositeSellPrice), 
                Double.parseDouble(compositeRebundleLvl), compositeDescription, fileName, packageItemList, 
                itemInventotySKUVec);
        em.persist(ciEntity);
        return true;
    }
    
    @Override
    public List<Vector> viewCompositeItemList(){
        Query q = em.createQuery("SELECT c FROM CompositeItem c");
        List<Vector> compItemList = new ArrayList<Vector>();
        
        for(Object o: q.getResultList()){
            CompositeItemEntity compE = (CompositeItemEntity) o;
            Vector compVec = new Vector();
            
            compVec.add(compE.getCompositeImagePath());
            compVec.add(compE.getCompositeName());
            compVec.add(compE.getCompositeSKU());
            compVec.add(compE.getCompositeQuantity());
            compVec.add(compE.getCompositeSellPrice());
            compVec.add(compE.getCompositeRebundleLvl());
            compItemList.add(compVec);
        }
        return compItemList;
    }
    
    @Override
    public Vector getCompositeItemInfo(String compositeIdentifier) {
        ciEntity = lookupCompositeItem(compositeIdentifier);
        Vector compositeInfoVec = new Vector();
        
        if (eEntity != null) {
            compositeInfoVec.add(ciEntity.getCompositeImagePath());
            compositeInfoVec.add(ciEntity.getCompositeName());
            compositeInfoVec.add(ciEntity.getCompositeDescription());
            compositeInfoVec.add(ciEntity.getCompositeSellPrice());
            compositeInfoVec.add(ciEntity.getCompositeQuantity());
            compositeInfoVec.add(ciEntity.getCompositeArrList());
            return compositeInfoVec;
        }
        return null;
    }
    
    @Override
    public List<Vector> getAssocCompItemInventoryInfo(String compositeIdentifier) {
        ciEntity = lookupCompositeItem(compositeIdentifier);
        List<Vector> assocItemInventoryList = new ArrayList<Vector>();
        
        for(int i = 0; i < ciEntity.getItemInventorySKUVec().size(); i++) {
            iEntity = lookupItem((String)ciEntity.getItemInventorySKUVec().get(i));
            Vector assocItemInventoryVec = new Vector();
            assocItemInventoryVec.add(iEntity.getItemImageDirPath());
            assocItemInventoryVec.add(iEntity.getItemName());
            assocItemInventoryVec.add(iEntity.getItemSKU());
            assocItemInventoryVec.add(iEntity.getItemQuantity());
            assocItemInventoryVec.add(iEntity.getItemSellingPrice());
            assocItemInventoryList.add(assocItemInventoryVec);
        }
        return assocItemInventoryList;
    }
    
    @Override
    public List<Vector> viewSalesOrderlist(){
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Query q = em.createQuery("SELECT s FROM SalesOrder s");
        List<Vector> salesOrderList = new ArrayList<Vector>();
        
        for(Object o: q.getResultList()){
            SalesOrderEntity salesOrder = (SalesOrderEntity) o;
            ContactEntity cust = salesOrder.getCustomer();
            Vector soVec = new Vector();
            /*
            Order of columns in view page:
            ID, creation date, status, full name, username, total amount NETT, 
            */
            soVec.add(salesOrder.getSalesOrderNumber());
            soVec.add(df.format(salesOrder.getCreationDateTime()));
            soVec.add(salesOrder.getStatus());
            soVec.add(cust.getContactSalutation()+" "+cust.getContactFirstName()+" "+cust.getContactLastName());
            soVec.add(cust.getContactUsername());           
            soVec.add(salesOrder.getTotalPrice()+salesOrder.getShippingAmt()-salesOrder.getDiscountAmt());
            salesOrderList.add(soVec);
        }
        return salesOrderList;
    }
    
    @Override
    public List<Vector> viewPaidSO(){
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        DecimalFormat decfor = new DecimalFormat("0.00");
        Query q = em.createQuery("SELECT s FROM SalesOrder s WHERE s.isPaid=true");
        List<Vector> paidSalesOrderList = new ArrayList<Vector>();
        
        for(Object o: q.getResultList()){
            SalesOrderEntity salesOrder = (SalesOrderEntity) o;
            Vector soVec = new Vector();
            soVec.add(salesOrder.getSalesOrderNumber().toString());
            soVec.add(salesOrder.getInvoice().getInvoiceNum().toString());
            soVec.add(salesOrder.getInvoice().getPaymentReferenceNum());
            soVec.add(decfor.format(salesOrder.getTotalPrice()+salesOrder.getShippingAmt()-salesOrder.getDiscountAmt()));
            soVec.add(""+df.format(salesOrder.getInvoice().getDateTime()));
            soVec.add(salesOrder.getInvoice().getCustomerNotes());
            paidSalesOrderList.add(soVec);
        }
        return paidSalesOrderList;
    }
    

    @Override
    public List<Vector> viewOutSO(){
        DecimalFormat decfor = new DecimalFormat("0.00");
        Query q = em.createQuery("SELECT s FROM SalesOrder s WHERE s.isPaid=false");
        List<Vector> outstandingSalesOrderList = new ArrayList<Vector>();
        
        for(Object o: q.getResultList()){
            SalesOrderEntity salesOrder = (SalesOrderEntity) o;
            ContactEntity cus = (ContactEntity) salesOrder.getCustomer();
            Vector soVec = new Vector();
            soVec.add(salesOrder.getSalesOrderNumber().toString());
            soVec.add(decfor.format(salesOrder.getTotalPrice()+salesOrder.getShippingAmt()-salesOrder.getDiscountAmt()));
            soVec.add(salesOrder.getStatus());
            soVec.add(""+cus.getContactSalutation()+" "+cus.getContactFirstName()+ " "+cus.getContactLastName());
            soVec.add(cus.getContactEmail());
            soVec.add(cus.getContactPhone());
            outstandingSalesOrderList.add(soVec);
        }
            return outstandingSalesOrderList;
    }
    
    @Override
    public void massManualReconciliation(ArrayList<Vector> manualRec){
        int size = manualRec.size();
        for (int i = 0; i < size; i++){
            Vector item = (Vector) manualRec.get(i);
            reconcile((String)item.get(0), (String) item.get(1));
        }
    }
    
    private void reconcile(String soNum, String txnNum){
        Query q = em.createQuery("SELECT s FROM SalesOrder s WHERE s.salesOrderNumber= :number");
        Long soNumber = Long.parseLong(soNum);
        q.setParameter("number", soNumber);
        SalesOrderEntity so = (SalesOrderEntity) q.getSingleResult();
        InvoiceEntity invoice1 = new InvoiceEntity();
        invoice1.setActive(true);
        invoice1.setCustomerNotes(so.getDeliveryNotes());
        invoice1.setContactUsername(so.getCustomer().getContactUsername());
        invoice1.setBillingAddress(so.getCustomer().getContactBillingAddress());
        invoice1.setShippingAddress(so.getCustomer().getContactShippingAddress());
        invoice1.setContactNum(so.getCustomer().getContactID());
        invoice1.setPaymentReferenceNum(txnNum);
        invoice1.setDiscountAmt(so.getDiscountAmt());
        invoice1.setShippingAmt(so.getShippingAmt());
        invoice1.setDate(new Date());
        invoice1.setStatus("Paid");
        so.setIsPaid(true);
        so.setInvoice(invoice1);
        em.persist(invoice1);
        em.persist(so);
    }
    
    
    @Override
    public ArrayList<ArrayList> viewItemList(){
        ArrayList<ArrayList> itemList = new ArrayList<>();
        Query q = em.createQuery("SELECT i FROM Item i WHERE i.activeStatus=true");
        
        for(Object o: q.getResultList()){
            ItemEntity item = (ItemEntity) o;
            ArrayList itemArr = new ArrayList();
            itemArr.add(item.getItemImageDirPath());
            itemArr.add(item.getItemName());
            itemArr.add(item.getItemSKU());
            itemArr.add(item.getItemSellingPrice());
            itemArr.add(item.getItemQuantity());
            itemList.add(itemArr);
        }       
        return itemList;        
    }
    
    @Override
    public boolean createItem(String itemImageDirPath, String itemSKU, String itemName, String itemDescription, String itemQuantity, String itemReorderLevel, String itemSellingPrice, String vendorID, String vendorProductCode) {
        try{
            ItemEntity item = new ItemEntity(itemSKU, itemName, itemDescription, Double.parseDouble(itemQuantity), Double.parseDouble(itemReorderLevel), Double.parseDouble(itemSellingPrice), itemImageDirPath, vendorID, vendorProductCode);
            em.persist(item);
            return true;
        }catch(Exception e){
            System.out.println("***NEW ITEM COULD NOT BE CREATED***");
            return false;
        }      
    }
    
    @Override
    public ArrayList viewItem(String itemSKU) {
        ArrayList itemDetails = new ArrayList();
        Query q = em.createQuery("SELECT i FROM Item i WHERE i.itemSKU = :chosenSKU");
        q.setParameter("chosenSKU", itemSKU);
        List resultList = q.getResultList();
        //below may throw no result exception or non unique result exception
        if(resultList.isEmpty()){
           System.out.println("**WMS VIEW ITEM: There is no such item in the database!**");
        }
        else{
            ItemEntity result = (ItemEntity)resultList.get(0);
      
            itemDetails.add(result.getItemName());
            itemDetails.add(result.getItemSKU());
            itemDetails.add(result.getVendorID());
            itemDetails.add(result.getVendorProductCode());
            itemDetails.add(result.getItemSellingPrice());
            itemDetails.add(result.getItemQuantity());
            itemDetails.add(result.getItemReorderLevel());
            itemDetails.add(result.getItemDescription());
            itemDetails.add(result.getItemImageDirPath());
        }      
        return itemDetails;
    }
    
    @Override
    public void deleteItem(String itemSKU){
        Query q = em.createQuery("SELECT i FROM Item i WHERE i.itemSKU = :chosenSKU");
        q.setParameter("chosenSKU", itemSKU);
        ItemEntity item = (ItemEntity)q.getSingleResult();
        //set this item to inactive!
        System.out.println("Setting "+itemSKU+" to inactive!");
        item.setActiveStatus(false);
    }
    
    @Override
    public boolean editItem(String itemImageDirPath, String itemSKU, String itemName, String itemDescription, String itemQuantity, String itemReorderLevel, String itemSellingPrice, String vendorID, String vendorProductCode){       
        try{
            Query q1 = em.createQuery("SELECT i FROM Item i WHERE i.itemSKU =:chosenSKU");
            q1.setParameter("chosenSKU", itemSKU);
            ItemEntity item = (ItemEntity) q1.getSingleResult();
            item.setItemImageDirPath(itemImageDirPath);
            item.setItemName(itemName);
            item.setItemDescription(itemDescription);
            item.setItemQuantity(Double.valueOf(itemQuantity));
            item.setItemReorderLevel(Double.valueOf(itemReorderLevel));
            item.setItemSellingPrice(Double.valueOf(itemSellingPrice));
            item.setVendorID(vendorID);
            item.setVendorProductCode(vendorProductCode);
            System.out.println("******ITEM EDITED********");
            return true;
        }catch(NumberFormatException e){
            return false;
        }      
    }
    
    @Override
    public List<Vector> viewInvoiceList() {//WORK IN PROGRESS
        List<Vector> invoiceList = new ArrayList<Vector>();
        /*SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        ContactEntity customer = null;
        //Extract invoices
        Query q = em.createQuery("SELECT i FROM Invoice i");
        for(Object o: q.getResultList()){
            InvoiceEntity invoiceE = (InvoiceEntity) o;
            Vector invoiceVec = new Vector();
            
            //Extract the customer based on the stored username
            /*Query q1 = em.createQuery("SELECT c FROM Contact c WHERE c.contactUsername =: username");
            q1.setParameter("username",invoiceE.getContactUsername());
            invoiceVec.add(invoiceE.getInvoiceNum());
            List result = q1.getResultList();
            if(!result.isEmpty()){
                Iterator it = result.iterator();
                customer = (ContactEntity)it.next();
            }*/
            /*invoiceVec.add(invoiceE.getInvoiceNum());
            //invoiceVec.add(df.format(invoiceE.getDateTime()));
            invoiceVec.add(invoiceE.getPaymentReferenceNum());
            invoiceVec.add(invoiceE.getContactUsername());
            /*invoiceVec.add(customer.getContactSalutation()
                    +" "+customer.getContactFirstName()
                    +" "+customer.getContactLastName());*/    
            /*invoiceVec.add(invoiceE.getShippingAmt()+invoiceE.getDiscountAmt());

            invoiceList.add(invoiceVec);
        }*/
        return invoiceList;
    }

    /* MISCELLANEOUS METHOD HELPERS */
    @Override
    public boolean empLogin(String empUsername, String empPassword) {
        /* Must perform hashing here, not on the servlet side. Otherwise will produce different hash values */
        String hashedPassword = "";
        try{ hashedPassword = encodePassword(empPassword); }
        catch(NoSuchAlgorithmException ex){ ex.printStackTrace(); }

        eEntity = new EmployeeEntity();
        try{
            Query q = em.createQuery("SELECT e FROM Employee e WHERE e.empUsername = :empUsername");
            q.setParameter("empUsername", empUsername);
            eEntity = (EmployeeEntity)q.getSingleResult();
        }
        catch(EntityNotFoundException enfe){
            System.out.println("ERROR: Employee cannot be found. " + enfe.getMessage());
            em.remove(eEntity);
            eEntity = null;
        }
        catch(NoResultException nre){
            System.out.println("ERROR: Employee does not exist. " + nre.getMessage());
            em.remove(eEntity);
            eEntity = null;
        }
        if(eEntity == null) { return false; }
        // if(eEntity.getEmpPassword().equals(hashedPassword)) { return true; }
        if(eEntity.getEmpPassword().equals(empPassword)) { return true; }
        return false;
    }
    
    public ContactEntity lookupContact(String emailAddress){
        ContactEntity ce = new ContactEntity();
        try{
            Query q = em.createQuery("SELECT c FROM Contact c WHERE c.contactEmail = :emailAddress");
            q.setParameter("emailAddress", emailAddress);
            ce = (ContactEntity)q.getSingleResult();
        }
        catch(EntityNotFoundException enfe){
            System.out.println("ERROR: Contact cannot be found. " + enfe.getMessage());
            em.remove(ce);
            ce = null;
        }
        catch(NoResultException nre){
            System.out.println("ERROR: Contact does not exist. " + nre.getMessage());
            em.remove(ce);
            ce = null;
        }
        return ce;
    }
    
    public EmployeeEntity lookupEmployee(String emailAddress){
        EmployeeEntity ee = new EmployeeEntity();
        try{
            Query q = em.createQuery("SELECT e FROM Employee e WHERE e.empEmail = :emailAddress");
            q.setParameter("emailAddress", emailAddress);
            ee = (EmployeeEntity)q.getSingleResult();
        }
        catch(EntityNotFoundException enfe){
            System.out.println("ERROR: Employee cannot be found. " + enfe.getMessage());
            em.remove(ee);
            ee = null;
        }
        catch(NoResultException nre){
            System.out.println("ERROR: Employee does not exist. " + nre.getMessage());
            em.remove(ee);
            ee = null;
        }
        return ee;
    }
    
    public CompositeItemEntity lookupCompositeItem(String compositeIdentifier){
        CompositeItemEntity ci = new CompositeItemEntity();
        try{
            Query q = em.createQuery("SELECT c FROM CompositeItem c WHERE c.compositeSKU = :compositeIdentifier");
            q.setParameter("compositeIdentifier", compositeIdentifier);
            ci = (CompositeItemEntity)q.getSingleResult();
        }
        catch(EntityNotFoundException enfe){
            System.out.println("ERROR: Composite Item cannot be found. " + enfe.getMessage());
            em.remove(ci);
            ci = null;
        }
        catch(NoResultException nre){
            System.out.println("ERROR: Composite Item does not exist. " + nre.getMessage());
            em.remove(ci);
            ci = null;
        }
        return ci;
    }
    
    public ItemEntity lookupItem(String itemInventorySKU){
        ItemEntity i = new ItemEntity();
        try{
            Query q = em.createQuery("SELECT i FROM Item i WHERE i.itemSKU = :itemInventorySKU");
            q.setParameter("itemInventorySKU", itemInventorySKU);
            i = (ItemEntity)q.getSingleResult();
        }
        catch(EntityNotFoundException enfe){
            System.out.println("ERROR: Item cannot be found. " + enfe.getMessage());
            em.remove(i);
            i = null;
        }
        catch(NoResultException nre){
            System.out.println("ERROR: Item does not exist. " + nre.getMessage());
            em.remove(i);
            i = null;
        }
        return i;
    }
    
    public String encodePassword(String password) throws NoSuchAlgorithmException {
        String hashedValue = "";
        
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] bytes = md.digest();
        
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < bytes.length; i++){
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            hashedValue = sb.toString();
        }      
        return hashedValue;
    }
}