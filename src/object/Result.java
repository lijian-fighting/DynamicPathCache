package object;

/**
 * @Author: LiJian
 * @Description:
 * @Date: Created in 16:29 2017/2/15
 * @Modified By: lijian
 */
public class Result {
    boolean Isweightupdate;
    boolean Ispathjoin;
    int querysize;
    int cachesize;
    int Tmax;
    double A;
    double N;
    double Ourhit;
    double Ourtime;
    double Ouraccur;
    double Our5accur;
    double Our10accur;
    double LRUhit;
    double LRUtime;
    double LRUaccur;
    double LRU5accur;
    double LRU10accur;
    double LFUhit;
    double LFUtime;
    double LFUaccur;
    double LFU5accur;
    double LFU10accur;
    double SPChit;
    double SPCtime;
    double SPCaccur;
    double SPC5accur;
    double SPC10accur;
    double Maxweight;
    public Result(boolean isweightupdate,boolean ispathjoin,int query,int cache,int tmax,double a,double n,
           double ourhit,double lruhit,double lfuhit, double spchit,
           double ourtime,double lrutime,double lfutime,double spctime,
           double ouraccur,double lruaccur,double lfuaccur, double spcaccur,
           double our5accur,double lru5accur,double lfu5accur, double spc5accur,
           double our10accur,double lru10accur,double lfu10accur, double spc10accur,
           double maxweight){
        this.Isweightupdate = isweightupdate;
        this.Ispathjoin = ispathjoin;
        this.querysize = query;
        this.cachesize = cache;
        this.Tmax = tmax;
        this.A = a;
        this.N = n;

        this.Ourhit = ourhit;
        this.LRUhit = lruhit;
        this.LFUhit = lfuhit;
        this.SPChit = spchit;

        this.Ourtime = ourtime;
        this.LRUtime = lrutime;
        this.LFUtime = lfutime;
        this.SPCtime = spctime;

        this.Ouraccur = ouraccur;
        this.LRUaccur = lruaccur;
        this.LFUaccur = lfuaccur;
        this.SPCaccur = spcaccur;

        this.Our5accur = our5accur;
        this.LRU5accur = lru5accur;
        this.LFU5accur = lfu5accur;
        this.SPC5accur = spc5accur;

        this.Our10accur = our10accur;
        this.LRU10accur = lru10accur;
        this.LFU10accur = lfu10accur;
        this.SPC10accur = spc10accur;

        this.Maxweight = maxweight;
    }
    public boolean getIsweightupdat(){
        return Isweightupdate;
    }
    public boolean getIspathjoin(){
        return Ispathjoin;
    }
    public int getQuerysize(){
        return querysize;
    }
    public int getCachesize(){
        return cachesize;
    }
    public int getTmax(){
        return Tmax;
    }
    public double getA(){
        return A;
    }
    public double getN(){
        return N;
    }

    public double getOurhit(){
        return Ourhit;
    }
    public double getLRUhit(){
        return LRUhit;
    }
    public double getLFUhit(){
        return LFUhit;
    }
    public double getSPChit(){
        return SPChit;
    }

    public double getOurtime(){
        return Ourtime;
    }
    public double getLRUtime(){
        return LRUtime;
    }
    public double getLFUtime(){
        return LFUtime;
    }
    public double getSPCtime(){
        return SPCtime;
    }
    public double getOuraccur(){
        return Ouraccur;
    }
    public double getLRUaccur(){
        return LRUaccur;
    }
    public double getLFUaccur(){
        return LFUaccur;
    }
    public double getSPCaccur(){
        return SPCaccur;
    }
    public double getOur5accur(){
        return Our5accur;
    }
    public double getLRU5accur(){
        return LRU5accur;
    }
    public double getLFU5accur(){
        return LFU5accur;
    }
    public double getSPC5accur(){
        return SPC5accur;
    }
    public double getOur10accur(){
        return Our10accur;
    }
    public double getLRU10accur(){
        return LRU10accur;
    }
    public double getLFU10accur(){
        return LFU10accur;
    }
    public double getSPC10accur(){
        return SPC10accur;
    }
    public double getMaxweight(){
        return Maxweight;
    }
}
