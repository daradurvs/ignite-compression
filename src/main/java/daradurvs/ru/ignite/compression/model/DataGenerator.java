package daradurvs.ru.ignite.compression.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class DataGenerator {
    public static final String RES_MODEL_DIR = "src/main/resources/model/";
    public static final String PERSON_CSV = "person_data.csv";
    public static final String AUDIT_CSV = "audit_symbols_data.csv";
    public static final String AUDIT2_CSV = "audit_text_data.csv";

    private static final int ENTRIES = 1000;

    public static void main(String[] args) throws IOException {
        List<String> names = Files.readAllLines(Paths.get(RES_MODEL_DIR + "random_1000_names.txt"), StandardCharsets.UTF_8);
        assert names.size() == ENTRIES;

        try (BufferedWriter pw = Files.newBufferedWriter(Paths.get(RES_MODEL_DIR + PERSON_CSV), StandardCharsets.UTF_8);
             BufferedWriter aw = Files.newBufferedWriter(Paths.get(RES_MODEL_DIR + AUDIT_CSV), StandardCharsets.UTF_8);
             BufferedWriter a2w = Files.newBufferedWriter(Paths.get(RES_MODEL_DIR + AUDIT2_CSV), StandardCharsets.UTF_8)) {

            for (int id = 12345001, i = 0; i < ENTRIES; ++id, i++) {
                pw.write(createPersonLine(id, names.get(i)));
                pw.newLine();

                aw.write(createAuditLine(id, LINE, ""));
                aw.newLine();

                a2w.write(createAuditLine(id, LINE2, " "));
                a2w.newLine();
            }
        }
    }

    private static String createPersonLine(long id, String name) {
        return id +
            ";" +
            Integer.toString(14 + new Random().nextInt(80)) +
            ";" +
            (name.endsWith("а") ? "false" : "true") +
            ";" +
            name +
            ";" +
            Long.toString(9000000000L + new Random().nextInt(999999999)) +
            ";";
    }

    private static String createAuditLine(long id, String line, String delim) {
        return id +
            ";" +
            UUID.randomUUID().toString() +
            ";" +
            createShuffledLine(line, delim) +
            ";";
    }

    private static String createShuffledLine(String line, String delim) {
        List<String> letters = Arrays.asList(line.split(delim));
        Collections.shuffle(letters);
        StringBuilder builder = new StringBuilder();

        for (String letter : letters)
            builder.append(letter);

        return builder.toString();
    }

    // 6144 kb
    private static final String LINE = "O9r.011624=7=034u8t$u9=0g3{Ж40V2s06,,ue,500К28o3s048atc-0bonuuI890002Or6a406>a26k5,00-58sCe77fo4230eg=99,4e03>01>138,l313e.80a,a60g9<e13550r,70om33=,2rlm,770260c80a]u44,020u174m9eju8F3}a79,5t04cf0089A0909e0,t7434,179c8,e60e2b749a9,B745101e0e24,0m,8<01m[974689k2,32,6,53,tv04gPs220[i0,taG01350h3233n00t8ot5i9130603fp3u63,0P60nnr0l04k:OI58ig007Pr4e039l6295i3>08ua0t060,,p44il2,516a,6,-,062470I6=05t0,5B75e7,0la14cp0e038n929l4,p09l03B47879o8r78,93,01D20o0{8u,4578e,0stes80c6D088=c0r89d594050,fM5u32t076066,748001u79nug@07e1,l7hDp1О,7P@8Ua-]0>91664,440r,00878900a1=cn1223e71206uuj-7433In18i0076,01tL388l01037r7742D9e946r12401,a<045Pi0m.3ol,l=e255,8Dru=6,42t5e,00$r1606m=50s0553As0c19901D=sf58a6.463arean=3ta1c,0=I],qf-2000n,d38-e9si48a000F208g00t0rea<15,n0o906e,5r6890so0046t79,20en0312o0L071u8[l>3p,00z467sn1t96W0lr0i7r6,rf70f4il58305=sc3562/e600,0a2U2930002@00u9Of297302nru00e910i0[5=400n,0gs08aG9F8re1,35080<02l2l940T8T07П8gl0024343uf152m,6319,3=-80e4e1=e507-471t8906l.050IM2,,975,anxfIay9f05e5or1l.ek0s1l99=al=18k6i63<7s3333,qIl,b4a41ed3nS,e85ne5s0p0ites1p8a10594It7sU6t0,{t39o029032t0,00eus1-09e794.2en10s0S=8q55i60n4a,3tt06ueoz0eee1d81R>,==,cl@e0,0400,040it6u-29e27u4r6404703n6bac,n37197693gr8m0t039{.cgp46<04<tr708-I700k00,87]650u9i0707073aOe1130n0u040,05>r\\\"o3s04e4018646n9,caIu0a751mt4i9-ne6,8=0,0g,507,80010309l,002i5eor3n01009e07e6,6499,2,]<,60-Cd24r7a,620o4860,00r4tose10y0a1na{7erОee099t2909r7t.17uiIr0g049030,0<u1M7u290:2-3,002300au900$0e00rl7x0Tf6030E24l206008GeC.7a523,.5-TL88I,onW,0s64r,ne58ap.0i98,157r983g,5,14n963,352a0tritB9ft8,80613026201e258>g0,59nk,37=I7280eeney768,0t8,esr2u=e9e35i,6a3=6}10642628I,,mn8k0130346-ur3u0-ee7g2202,820ele080]d3nLo,d5Аa2l>,2a4>l2488a=7,3,0il2ee8,9n002003904g39i05720o7mZe-01D,6ne9\\\"33k7002a318R007n7,06ratu60rn880,e60ei08ЫoOa3d2c04r7a107er09ey.,nNmo06=l9m90l,896=-.oe82636atI3e6cr1,e3d00n20t44,000ls328A02t2.00euu003g,,c19pattI,082=ta0i81e0r=33e700390,900}tto390ia42sao0tds8008O7i7383030o4v9Is7o92a,u93r738te4-na16r29Ru5D05k8,e10_cak,tr8r0V42a-7=3uf2c1c]tFfa41r0301e6408r=0b1n<20l0autui0m99@.2=98-871=et0242f0010r0o,43u5ul9195p546e0nC2tre70ran=72oc2,a2=80o6029r38,6ez326l004,ssa6s,l00r,009t04909012103f9402tno4ce990507u40,80IttO6047d9e720e600P0p,0lc9466ef,81e2e983-7:0dc1,887tat5u7ioi1,clD59a082=9,oue9c6[s8205r061c20a-0r7o022rnd13ares787513eu9do9Fu90ep,0m908ai4ea2P1t6r91026tm392031123=,0rD397,0A67ea640090,,<5775m=e8e76227608wa08n0D7l099090a0511g1E20eP06,{8ek3an.e3rl60u90899=92279o651Bmpk86110f3}Bu26d001m0r0d0980a7@11tf50n10d0030-,478au999of50e5w9s01e48,d64al0_t,e5072=2030080sr560aa,e9o5g5,e0gТ76n70=193m141I68n16,29986e20a21,9o49s4,9e36a0n2y0r1o6200815902rm7,4e36a01m010[Н0684c980,06gSu39dr,0t0t,b32mm9,7<<93821ln>0r=7cx>47040001030sL99p1a,221360c040:0152408c3me0t01c<00,2t6910tZ62e1p,4,280gi4r=,0unn0640,29pk1ff7a0peror=21,,6e=1e3u=3F99c9e1en6e0e,00er291c83,nmo93c2rg2,ec22,2umL0<aS017r,am1t07S3f03,etam02l13026a40633230.,6090548,>u420181,f4rb06d,,96.l0ie2544ta,7f,45il7400029p000A02jI0,e0903A201l@,er81275u,220xmte720=0152,,p>,7s5r,342ou,091ie07i,g0it03n666il886aI00c94t.060t029s1028=8,a967f9e,0a0w008=7a1t12ei=,f00058l8028,4S091Doofi021-2rds[A5e63C6,0[92,n<0l7>0l550840,rn3ra04750,860Cb00=ee76r4ts,v,8lt19>1aiua=0ed6oa00ei857n10e860p4rta06n,p6009,i,16orln7gl66p7i0B9e7t4312t4{C[,9t.621,0t47-0sr1,1nDu070,ee302ec4,r47cdad8,sc.0,u43uu[o,05au30u,5,05ee70200e1n6N647o36e70.ds9r00rSy3r4tslt01I,n3I49,9pfee2n247rl,0le3e0Вn16etlh38<]07r20}7e40di05288l8a5010970N8l,9cu9=4a,6u1f0n6c=r024b<30808lt52t206485@8[sf800s=11656Omatef20-6823/le11a201874c7,06n303IZnonF00f2u85231=1=5,064r.1pa0u9nlirr0i,49tbu1aa0a0,1<00l001,0,11eO204817r604050dl9=e0,ul2=0,a7e0,0851Kt-03kI060I30189244o=roea396I9s52,ree8ui3,s85n97cD,ls0w03600909I0j83306750pu732878A47Olt9D95c[f55023mti01,gu0027fod,26s0<3n3p4200S40114i777x,t71nt22gr8t730501-S7gnewar7123q-41t=,p4u6=392097a<S4c900o803V,o3p5012fs66u5m10905=tttt4070c,4m7659ld97T0s1e0el1110c0e2t0,3,9e5899,14004ru9927i,078=710eer,uu04706.I{1{0.1<04n097hr2o,t7u1pd5,0e0m0ms2r0u2=4l21B=0a01w.D412q0m0e67880a,e,rw00a4020n8-k<n83w31kP76309,=s,0k60u9013,6dpl4D4980ue1e9,N40Д030,77,Pog{10sf10i.0n0of,130c5u8sr=0-07.r6ln6N38711012od80,t39>e0r70uSu0mSt376Tg100oenee9ah600]5m94e0t8097042-,sn0e50460440m5,0m17s7t8938,>00x240mo02ni08,997917Ny08,0o0180sc44u,=,sr0439073015@1a0,90Е74sIa0n10t66a0x3T1rb,706r4r56ao6931ce346C0,081l12a1R6=10s1547091,o3ii75020f2e9=c7-74e-t22=50ln76508l,,ea,f1t349enF,me-07o021r,k3e0200,er0B7F06973,0nee083010a-73005f3t0e1ac8I4C80,944e26n08s30ru4502a00,0oe0p35ei8e1807,t,1o87@03c80{9n=O8f4938T6092507s11372742063447580Aue060l6e730t8t078l,5t,lac9uf55=I,9o8<771[80<tr100064cf0e,09C74Intei08PA02408u01308lD0l0830i40e0,ui6@=Cs19ab5ye2o711=lxs99el0=301g5u.250l82i,8d20082r4a85a030Н395ds84ftc90084e83e0s2=,57,,7ede,t00601vn6401,03066p0e02970ueS07i7,94et901u3u4la60t00i0I00003=37-75У,i10n7v77012=r,t0,ua90s2=t138t>T2,001ri6w70l5886k80nO,11:C071t00d659m,sa6,3u08esuf14r}8ee9ins0v4052-7900>lr6TC5}6l905714003i0,734205g0A1,,0t0yl1c410r1P904f@e89044,5=gg021la=e>0lr701,,0t85472n5.,930sr87segeei06lT77t140udef,0N0,8l0-e-eoc4I833s06<p}t5twО30b51rt0<0u6oroI,00,y18,0g7r424eukn05p2ane950y9T2a01p=k,404r>11,0,87=0M01ac,f88ir7g4469,s00Z2c8,940001,71657,00801=a024,91,F-9884ot31013740r1r071p0@00>A3l1,g7086Rl0e6,ei0iusN2,1g0247n0<0==I8nt89es<l,r01nrI=,aIc6017d0r24u0002v8o0rqrm207reoa07,Smedtb00a3e00,0,8lao206m7s3219=0l559=Cu2038,59x26fa=0300,ndn09,7e7r65y09]35,f6306620117.2580rl5102030201-8b49d503vf9xt01,494pe0450t0i0=0nu>f8107Te,s19e700=e002e,0r1m4a[,2s0,0yuu2t22a91,I77128msn1nbak951770,U78709093r,965,tx407x200491nTI19fk29r,09a1041l6449o,08m0I,s582Wlt94t1ot1r6360gf02v30tt4tTre0=0n7280dastoie2tst52689u,s,79F025n7tt4e70mr,6>719-0020l515003@1>16s4088=307,082631@3u050401u01el502415ni9nu49f020cn0200,09069u,09,845n179=1aC44e4ea1Srer,,2iltI2a700te83t062{n8670e,,01ufuo0o00sW8,i,030t>36ylgn0u29p5070n<n673-um2s400m8t5S076,0n2d-t,ute5e8ur00D2ЕC2k12e2b1l4sl0103r9O40P00txD65769,7,1-s8e4316t,085,96,r0c008e6lp,8i31e,s5b040НcOe2a0s08l02300a009d27803h8d6805205e00n5mCC7L00,lfi57,=f02k78,06a,t79i336fe9u6285}013S197T,1,0:0d055g,0540e8205,70i=g9o,e14li0CmC=5nCaru,i6,,07C1l818707,f0p272rd100F=32Й@86k87g7Z6u7p,08003=81Ta58ttw3r=,6051081371r6,8r09u0134iMq1Nr066.o14N";
    private static final String LINE2 = "In signal processing, data compression, source coding, or bit-rate reduction involves encoding information using fewer bits than the original representation. Compression can be either lossy or lossless. Lossless compression reduces bits by identifying and eliminating statistical redundancy. No information is lost in lossless compression. Lossy compression reduces bits by removing unnecessary or less important information. The process of reducing the size of a data file is referred to as data compression. In the context of data transmission, it is called source coding (encoding done at the source of the data before it is stored or transmitted) in opposition to channel coding. Compression is useful because it reduces resources required to store and transmit data. Computational resources are consumed in the compression process and, usually, in the reversal of the process (decompression). Data compression is subject to a space–time complexity trade-off. For instance, a compression scheme for video may require expensive hardware for the video to be decompressed fast enough to be viewed as it is being decompressed, and the option to decompress the video in full before watching it may be inconvenient or require additional storage. The design of data compression schemes involves trade-offs among various factors, including the degree of compression, the amount of distortion introduced (when using lossy data compression), and the computational resources required to compress and decompress the data. Lossless data compression algorithms usually exploit statistical redundancy to represent data without losing any information, so that the process is reversible. Lossless compression is possible because most real-world data exhibits statistical redundancy. For example, an image may have areas of color that do not change over several pixels instead of coding \"red pixel, red pixel, ...\" the data may be encoded as \"279 red pixels\". This is a basic example of run-length encoding there are many schemes to reduce file size by eliminating redundancy.  The Lempel–Ziv (LZ) compression methods are among the most popular algorithms for lossless storage. DEFLATE is a variation on LZ optimized for decompression speed and compression ratio, but compression can be slow. DEFLATE is used in PKZIP, Gzip, and PNG. LZW (Lempel–Ziv–Welch) is used in GIF images. LZ methods use a table-based compression model where table entries are substituted for repeated strings of data. For most LZ methods, this table is generated dynamically from earlier data in the input. The table itself is often Huffman encoded (e.g. SHRI, LZX). Current LZ-based coding schemes that perform well are Brotli and LZX. LZX is used in Microsoft's CAB format. The best modern lossless compressors use probabilistic models, such as prediction by partial matching. The Burrows–Wheeler transform can also be viewed as an indirect form of statistical modelling. The class of grammar-based codes are gaining popularity because they can compress highly repetitive input extremely effectively, for instance, a biological data collection of the same or closely related species, a huge versioned document collection, internet archival, etc. The basic task of grammar-based codes is constructing a context-free grammar deriving a single string. Sequitur and Re-Pair are practical grammar compression algorithms for which software is publicly available. In a further refinement of the direct use of probabilistic modelling, statistical estimates can be coupled to an algorithm called arithmetic coding. Arithmetic coding is a more modern coding technique that uses the mathematical calculations of a finite-state machine to produce a string of encoded bits from a series of input data symbols. It can achieve superior compression to other techniques such as the better-known Huffman algorithm. It uses an internal memory state to avoid the need to perform a one-to-one mapping of individual input symbols to distinct representations that use an integer number of bits, and it clears out the internal memory only after encoding the entire string of data symbols. Arithmetic coding applies especially well to adaptive data compression tasks where the statistics vary and are context-dependent, as it can be easily coupled with an adaptive model of the probability distribution of the input data. An early example of the use of arithmetic coding was its use as an optional (but not widely used) feature of the JPEG image coding standard. It has since been applied in various other designs including H.264/MPEG-4 AVC and HEVC for video coding.[citation] Lossy data compression is the converse of lossless data compression. In these schemes, some loss of information is acceptable. Dropping nonessential detail from the data source can save storage space. Lossy data compression schemes are designed by research on how people perceive the data in question. For example, the human eye is more sensitive to subtle variations in luminance than it is to the variations in color. JPEG image compression works in part by rounding off nonessential bits of information. There is a corresponding trade-off between preserving information and reducing size. A number of popular compression formats exploit these perceptual differences, including those used in music files, images, and video.  Lossy image compression can be used in digital cameras, to increase storage capacities with minimal degradation of picture quality. Similarly, DVDs use the lossy MPEG-2 video coding format for video compression.  In lossy audio compression, methods of psychoacoustics are used to remove non-audible (or less audible) components of the audio signal. Compression of human speech is often performed with even more specialized techniques speech coding, or voice coding, is sometimes distinguished as a separate discipline from audio compression. Different audio and speech compression standards are listed under audio coding formats. The data compression ratio can serve as a measure of the complexity of a data set or signal, in particular it is used to approximate the algorithmic complexity.";
}
