package algorithms;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UDGwithDS extends UDG { // dominating set
  public static double epsilon = 0.5;         // (1+epsilon) = a bound for the local DS
  public static double ro      = 1+epsilon; // compute a DS of cardinality no more than (1+epsilon) the size of min DS 
  public static int sizeMinNiebergHurink = 3; ///
  public static Map<UDG,UDG> collections = new HashMap<UDG,UDG>();
  public static int counter2=1;
  
  public UDGwithDS(ArrayList<Vertex> vertex) {
    super(vertex);
    this.isSolution                   = (solutionCandidat)     -> { return hasAsDS(solutionCandidat); }; 
    this.shouldContinueGreedy         = (currentSolution,rest) -> { return rest.isNotEmpty();}; // ok
    this.toRemoveBeforeContinueGreedy = (pointsToRemove)       -> { return new UDG(neighborhoodWithCentralPoint(pointsToRemove).vertices);}; 
    this.shouldTryToReplace2Points    = (Vertex p1, Vertex p2) -> { return p1.distance(p2)<4*edgeThreshold; }; /// ?
    this.shouldTryToReplace3Points    = (Vertex p1, Vertex p2, Vertex p3)-> { /// ?
      return  (p1.distance(p2)<4*edgeThreshold && p2.distance(p3)<4*edgeThreshold) && (p3.distance(p1)<4*edgeThreshold);
    }; 
  }

  public UDGwithDS(Vertex p) {
	this(new ArrayList<Vertex>());
	this.add(p);
  }

  public UDGwithDS(UDG g) {
	this(g.vertices);
  }

  public UDGwithDS() {
	this(new ArrayList<Vertex>());
  }

  
  public UDG ds() {
    if(!this.isConnected()) {
      System.out.println("the input graph must be connected ");
      return new UDG();
    }

    // get first solution:
	// UDG ds = greedyAlgo(); 
    
    UDG ds = dsNiebergHurink();
    
    // optimize the first solution: 
    // ds = repeatWhileCanDoBetter(ds,this.tryToReplace3by2); // too long
    ds = repeatWhileCanDoBetter(ds,this.tryToReplace2by1); 
    ds = repeatWhileCanDoBetter(ds,this.tryToRemovePoints); 

    return ds;
  }

  public UDGwithDS dsNiebergHurink() {
	//System.out.println((UDG.counter++)+" dsNiebergHurink");
	//if(UDG.counter>10) return new UDGwithDS();
	//System.out.println("!aPathOf3hopsOrLongerExiste() "+(!aPathOf3hopsOrLongerExiste())+" in "+this.toString());
	if(this.size()<sizeMinNiebergHurink || !this.aPathOf3hopsOrLongerExiste())
      return new UDGwithDS(this.dsLitleGraph()); 

	UDGwithDS V  = new UDGwithDS(this.clone()); // V=rest
    while(V.size()>0) {
   	  System.out.println("while V "+V.toString());
   	  V.computeNextCollection();
  	  //System.out.println("collections "+collections.toString());
   	  //System.out.println("from V "+V.toString()+" remove "+collections.keySet().stream().toList().toString());
   	  V.removeAll(collections.keySet());
  	  //if(UDGwithDS.counter2++>10) break;
    }
    
    UDG ds = new UDGwithDS(UDG.unionOf(new ArrayList<UDG>(collections.values().stream().toList())));
    return (UDGwithDS) ds;
  }
 
  private void computeNextCollection() {
	System.out.println("computeNextCollection this="+this.toString());
	UDGwithDS Nr0 = new UDGwithDS(this.theMostConnectedPoint());
	UDGwithDS DNr0 = Nr0.dsLitleGraph();
	//System.out.println("Nr0  "+Nr0.toString()+", DNr0 = "+DNr0.toString());
    UDGwithDS Nr1  = new UDGwithDS(this.neighborhoodWithInitialPoints(Nr0));
    UDGwithDS DNr1 = Nr1.dsLitleGraph();
	//System.out.println("Nr1  "+Nr1.toString()+", DNr1 = "+DNr1.toString());
    UDGwithDS Nr2  = new UDGwithDS(this.neighborhoodWithInitialPoints(Nr1));
    UDGwithDS DNr2 = Nr2.dsLitleGraph();
	//System.out.println("Nr2  "+Nr2.toString()+", DNr2 = "+DNr2.toString());

	while(DNr2.size()>ro*DNr0.size()) {
      System.out.println("while "+DNr2.size()+">"+ro+"*"+DNr0.size()+" continue");
	  Nr0  = Nr1;
      DNr0 = DNr1;
      //System.out.println("Nr0  "+Nr0.toString()+", DNr0 = "+DNr0.toString());
      Nr1  = Nr2;
      DNr1 = DNr2;
  	  //System.out.println("Nr1  "+Nr1.toString()+", DNr1 = "+DNr1.toString());
      Nr2  = new UDGwithDS(this.neighborhoodWithInitialPoints(Nr2));
      DNr2 = Nr2.dsLitleGraph();
  	  //System.out.println("Nr2  "+Nr2.toString()+", DNr2 = "+DNr2.toString());
    } 
	collections.put(Nr2,DNr2);
  }
  
  public UDGwithDS dsLitleGraph() {
	UDG ds = greedyAlgo(); 
    // ds = repeatWhileCanDoBetter(ds,this.tryToReplace3by2); // long, no utility in the example
    ds = repeatWhileCanDoBetter(ds,this.tryToReplace2by1); 
    ds = repeatWhileCanDoBetter(ds,this.tryToRemovePoints); 
	//System.out.println("dsLitleGraph ="+ds);
    return new UDGwithDS(ds);	  
  }
}

/*
671 409
664 402
454 252
804 315
679 380
705 313
975 274
777 576
820 362
410 526
610 490
669 546
664 407
459 235
702 526
728 338
746 423
619 218
829 536
848 322
980 578
979 464
648 434
399 278
788 326
436 463
671 423
666 247
941 281
490 508
994 551
786 340
571 393
454 593
455 350
692 272
950 560
356 501
937 307
691 594
978 238
992 390
932 470
411 356
694 220
763 322
498 294
974 243
894 334
964 500
693 531
422 388
412 262
472 478
845 514
634 601
660 619
878 238
708 317
718 491
826 492
974 437
435 595
434 509
585 255
634 297
415 366
953 248
906 581
481 376
839 340
959 596
473 472
888 476
814 548
434 550
708 645
501 538
560 604
855 646
763 614
818 445
750 563
914 269
439 429
866 582
932 353
696 568
400 428
405 467
856 414
622 506
628 230
983 319
940 584
968 274
916 232
833 419
411 310
653 235
682 445
571 646
600 502
655 239
958 426
422 466
853 593
743 308
856 356
568 502
861 641
657 258
592 541
550 263
638 577
473 298
755 426
688 456
597 375
570 524
435 304
540 295
608 440
427 334
537 259
652 228
452 268
568 240
653 454
586 397
776 317
668 266
458 499
580 484
430 321
530 283
619 360
578 562
417 500
795 571
467 542
824 364
617 418
951 262
524 449
523 395
642 565
949 409
589 225
928 519
436 387
509 573
707 562
996 376
885 399
878 485
861 233
996 471
655 207
473 538
400 473
407 581
747 387
422 477
916 470
919 467
967 461
677 528
719 318
480 290
544 482
694 289
587 555
584 447
437 206
479 259
609 411
926 378
743 329
515 291
640 490
710 460
751 219
983 338
434 361
740 247
795 304
506 503
789 422
482 623
850 577
796 520
428 263
707 354
 */
/*
400 320
400 360
400 400
400 440
400 480
400 520
400 560
400 600
400 640
440 320
440 360
440 400
440 440
440 480
440 520
440 560
440 600
440 640
*/