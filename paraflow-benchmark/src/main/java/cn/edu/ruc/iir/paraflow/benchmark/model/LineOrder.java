package cn.edu.ruc.iir.paraflow.benchmark.model;

import cn.edu.ruc.iir.paraflow.commons.ParaflowRecord;

/**
 * paraflow
 *
 * @author guodong
 */
public class LineOrder
        extends ParaflowRecord
        implements Model
{
    private long rowNumber;
    private Object[] values;

    public LineOrder()
    {
        this.values = new Object[23];
    }

    public LineOrder(
            long rowNumber,
            long lineOrderKey,
            long customerKey,
            byte[] orderStatus,
            double totalPrice,
            int orderDate,
            byte[] orderPriority,
            byte[] clerk,
            int shipPriority,
            byte[] orderComment,
            int lineNumber,
            double quantity,
            double extendedPrice,
            double discount,
            double tax,
            byte[] returnFlag,
            byte[] lineStatus,
            int shipDate,
            int commitDate,
            int receiptDate,
            byte[] shipInstructions,
            byte[] shipMode,
            byte[] lineitemComment,
            long creation)
    {
        this.rowNumber = rowNumber;
        this.values = new Object[]{lineOrderKey, customerKey, orderStatus, totalPrice, orderDate, orderPriority,
                clerk, shipPriority, orderComment, lineNumber, quantity / 100.0d, extendedPrice / 100.0d, discount / 100.0d,
                tax / 100.0d, returnFlag, lineStatus, shipDate, commitDate, receiptDate, shipInstructions, shipMode,
                lineitemComment, creation};
//        System.out.println("LineOrder{" +
//                "lineOrderKey=" + lineOrderKey +
//                ", customerKey=" + customerKey +
//                ", orderStatus=" + new String(orderStatus) + "_" + new String(orderStatus).length() +
//                ", totalPrice=" + totalPrice +
//                ", orderDate=" + orderDate +
//                ", orderPriority=" + new String(orderPriority) + "_" + new String(orderPriority).length() +
//                ", clerk=" + new String(clerk) + "_" + new String(clerk).length() +
//                ", shipPriority=" + shipPriority +
//                ", orderComment=" + new String(orderComment) + "_" + new String(orderComment).length() +
//                ", lineNumber=" + lineNumber +
//                ", quantity=" + quantity +
//                ", extendedPrice=" + extendedPrice +
//                ", discount=" + discount +
//                ", tax=" + tax +
//                ", returnFlag=" + new String(returnFlag) + "_" + new String(returnFlag).length() +
//                ", lineStatus=" + new String(lineStatus) + "_" + new String(lineStatus).length() +
//                ", shipDate=" + shipDate +
//                ", commitDate=" + commitDate +
//                ", receiptDate=" + receiptDate +
//                ", shipInstructions=" + new String(shipInstructions) + "_" + new String(shipInstructions).length() +
//                ", shipMode=" + new String(shipMode) +  "_" + new String(shipMode).length() +
//                ", lineitemComment=" + new String(lineitemComment) + "_" + new String(lineitemComment).length() +
//                ", creation=" + creation +
//                '}');
    }

    @Override
    public Object getValue(int idx)
    {
        return values[idx];
    }

    public long getLineOrderKey()
    {
        return (long) this.values[0];
    }

    public void setLineOrderKey(long lineOrderKey)
    {
        this.values[0] = lineOrderKey;
    }

    public long getCustomerKey()
    {
        return (long) values[1];
    }

    public void setCustomerKey(long customerKey)
    {
        this.values[1] = customerKey;
    }

    public char getOrderStatus()
    {
        return (char) values[2];
    }

    public void setOrderStatus(char orderStatus)
    {
        this.values[2] = orderStatus;
    }

    public double getTotalPrice()
    {
        return (double) values[3];
    }

    public void setTotalPrice(double totalPrice)
    {
        this.values[3] = totalPrice;
    }

    public long getTotalPriceInCents()
    {
        return (long) values[3];
    }

    public int getOrderDate()
    {
        return (int) values[4];
    }

    public void setOrderDate(int orderDate)
    {
        this.values[4] = orderDate;
    }

    public String getOrderPriority()
    {
        return (String) values[5];
    }

    public void setOrderPriority(String orderPriority)
    {
        this.values[5] = orderPriority;
    }

    public String getClerk()
    {
        return (String) values[6];
    }

    public void setClerk(String clerk)
    {
        this.values[6] = clerk;
    }

    public int getShipPriority()
    {
        return (int) values[7];
    }

    public void setShipPriority(int shipPriority)
    {
        this.values[7] = shipPriority;
    }

    public String getOrderComment()
    {
        return (String) values[8];
    }

    public void setOrderComment(String orderComment)
    {
        this.values[8] = orderComment;
    }

    public int getLineNumber()
    {
        return (int) values[9];
    }

    public void setLineNumber(int lineNumber)
    {
        this.values[9] = lineNumber;
    }

    public double getQuantity()
    {
        return (double) values[10];
    }

    public void setQuantity(double quantity)
    {
        this.values[10] = quantity;
    }

    public double getExtendedPrice()
    {
        return (double) values[11];
    }

    public void setExtendedPrice(double extendedPrice)
    {
        this.values[11] = extendedPrice;
    }

    public double getDiscount()
    {
        return (double) values[12];
    }

    public void setDiscount(double discount)
    {
        this.values[12] = discount;
    }

    public double getTax()
    {
        return (double) values[13];
    }

    public void setTax(double tax)
    {
        this.values[13] = tax;
    }

    public String getReturnFlag()
    {
        return (String) values[14];
    }

    public void setReturnFlag(String returnFlag)
    {
        this.values[14] = returnFlag;
    }

    public char getLineStatus()
    {
        return (char) values[15];
    }

    public void setLineStatus(char lineStatus)
    {
        this.values[15] = lineStatus;
    }

    public int getShipDate()
    {
        return (int) values[16];
    }

    public void setShipDate(int shipDate)
    {
        this.values[16] = shipDate;
    }

    public int getCommitDate()
    {
        return (int) values[17];
    }

    public void setCommitDate(int commitDate)
    {
        this.values[17] = commitDate;
    }

    public int getReceiptDate()
    {
        return (int) values[18];
    }

    public void setReceiptDate(int receiptDate)
    {
        this.values[18] = receiptDate;
    }

    public String getShipInstructions()
    {
        return (String) values[19];
    }

    public void setShipInstructions(String shipInstructions)
    {
        this.values[19] = shipInstructions;
    }

    public String getShipMode()
    {
        return (String) values[20];
    }

    public void setShipMode(String shipMode)
    {
        this.values[20] = shipMode;
    }

    public String getLineitemComment()
    {
        return (String) values[21];
    }

    public void setLineitemComment(String lineitemComment)
    {
        this.values[21] = lineitemComment;
    }

    public long getCreation()
    {
        return (long) values[22];
    }

    public void setCreation(long creation)
    {
        this.values[22] = creation;
    }

    public long getRowNumber()
    {
        return rowNumber;
    }

    public void setRowNumber(long rowNumber)
    {
        this.rowNumber = rowNumber;
    }

    public String toLine()
    {
        return "LineOrder{" +
                "lineOrderKey=" + values[0] +
                ", customerKey=" + values[1] +
                ", orderStatus=" + new String((byte[]) values[2]) +
                ", totalPrice=" + values[3] +
                ", orderDate=" + values[4] +
                ", orderPriority=" + new String((byte[]) values[5]) +
                ", clerk=" + new String((byte[]) values[6]) +
                ", shipPriority=" + values[7] +
                ", orderComment=" + new String((byte[]) values[8]) +
                ", lineNumber=" + values[9] +
                ", quantity=" + values[10] +
                ", extendedPrice=" + values[11] +
                ", discount=" + values[12] +
                ", tax=" + values[13] +
                ", returnFlag=" + new String((byte[]) values[14]) +
                ", lineStatus=" + new String((byte[]) values[15]) +
                ", shipDate=" + values[16] +
                ", commitDate=" + values[17] +
                ", receiptDate=" + values[18] +
                ", shipInstructions=" + new String((byte[]) values[19]) +
                ", shipMode=" + new String((byte[]) values[20]) +
                ", lineitemComment=" + new String((byte[]) values[21]) +
                ", creation=" + values[22] +
                '}';
    }

    public Object getFiled(int index)
    {
        if (index == 0) {
            return values[0];
        }
        else if (index == 1) {
            return values[1];
        }
        else if (index == 2) {
            return new String((byte[]) values[2]);
        }
        else if (index == 3) {
            return values[3];
        }
        else if (index == 4) {
            return values[4];
        }
        else if (index == 5) {
            return new String((byte[]) values[5]);
        }
        else if (index == 6) {
            return new String((byte[]) values[6]);
        }
        else if (index == 7) {
            return values[7];
        }
        else if (index == 8) {
            return new String((byte[]) values[8]);
        }
        else if (index == 9) {
            return values[9];
        }
        else if (index == 10) {
            return values[10];
        }
        else if (index == 11) {
            return values[11];
        }
        else if (index == 12) {
            return values[12];
        }
        else if (index == 13) {
            return values[13];
        }
        else if (index == 14) {
            return new String((byte[]) values[14]);
        }
        else if (index == 15) {
            return new String((byte[]) values[15]);
        }
        else if (index == 16) {
            return values[16];
        }
        else if (index == 17) {
            return values[17];
        }
        else if (index == 18) {
            return values[18];
        }
        else if (index == 19) {
            return new String((byte[]) values[19]);
        }
        else if (index == 20) {
            return new String((byte[]) values[20]);
        }
        else if (index == 21) {
            return new String((byte[]) values[21]);
        }
        else if (index == 22) {
            return values[22];
        }
        try {
            throw new Exception("Error Field.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
